package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.entity.nosql.ReadReceipt;
import com.gulfnet.tmt.entity.sql.UserGroup;
import com.gulfnet.tmt.model.response.ChatResponse;
import com.gulfnet.tmt.model.response.GroupChatResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.repository.nosql.ReadReceiptRepository;
import com.gulfnet.tmt.repository.sql.UserGroupRepository;
import com.gulfnet.tmt.chatService.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ReadReceiptRepository readReceiptRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    // Process the one-to-one message and save to DataBase
    @MessageMapping("/chat/privateMessage")
    public void processPrivateMessage(Principal principal, Chat chat) {
        log.debug("Received message from principal: {}", principal);

        if (principal == null) {throw new IllegalStateException("Principal cannot be null");}
        String receiverId = chat.getReceiverId();
        if (receiverId == null) {throw new IllegalStateException("Recipient cannot be null");}

        Chat savedMsg = chatService.savePrivateMessage(chat);

        // save entry of message in read receipt collection after sending message
        log.info("Adding entry of read receipt for processed message");

        ReadReceipt readReceipt = new ReadReceipt();
        readReceipt.setChatId(savedMsg.getId());
        readReceipt.setConsumerId(savedMsg.getReceiverId());
        readReceipt.setConversationId(savedMsg.getConversationId());
        readReceipt.setDeliveredAt(savedMsg.getDateCreated());
        readReceipt.setRead(false);
        readReceiptRepository.save(readReceipt);

        // Send the message to the recipient's queue
        log.info("Message processing for private chat with following metadata: {}", savedMsg);
        simpMessagingTemplate.convertAndSendToUser(receiverId,"/queue/reply", savedMsg);
    }

    // Process the group message and save to DataBase
    @MessageMapping("/chat/groupMessage")
    public void processGroupMessage(Chat chat) {
        log.debug("Received message for group chat");

        String groupId = chat.getReceiverId();
        if (groupId == null) {
            throw new IllegalStateException("Group ID cannot be null");
        }
        Chat savedMsg = chatService.saveGroupMessage(chat);

        // save entry of message in read receipt for each user in group collection after sending message
        log.info("Adding entries of each user's read receipt in group for processed message");

        List<UserGroup> userGroupList = userGroupRepository.findAllByGroupId(UUID.fromString(savedMsg.getReceiverId()));
        for(UserGroup users: userGroupList) {
            ReadReceipt readReceipt = new ReadReceipt();
            readReceipt.setChatId(savedMsg.getId());
            readReceipt.setConsumerId(String.valueOf(users.getUser().getId()));
            readReceipt.setConversationId(savedMsg.getConversationId());
            readReceipt.setDeliveredAt(savedMsg.getDateCreated());
            readReceipt.setRead(false);
            readReceiptRepository.save(readReceipt);
        }

        // Send the message to the recipient's queue
        log.info("Message processing for group chat with following metadata: {}", savedMsg);
        simpMessagingTemplate.convertAndSendToUser(groupId,"/queue/reply", savedMsg);
    }

    // Returns List of Chats(private) between sender and receiver by their conversationId
    @GetMapping("/messages/history/private-chat/{conversationId}")
    public ResponseDto<ChatResponse> getMessageHistoryForPrivateChat(@PathVariable("conversationId") String conversationId,
                                                            @PageableDefault(sort = {"dateCreated"}, direction = Sort.Direction.ASC, size = 100, value = 100)Pageable pageable){
        log.info("Request received for get messages with conversationId: {}", conversationId);
        return chatService.getChatMessagesForPrivate(conversationId, pageable);
    }

    // Returns List of Chats in group by groupId
    @GetMapping("/messages/history/group-chat/{groupId}")
    public ResponseDto<GroupChatResponse> getMessageHistoryForGroupChat(@PathVariable("groupId") String groupId,
                                                                        @PageableDefault(sort = {"dateCreated"}, direction = Sort.Direction.ASC, size = 100, value = 100)Pageable pageable){
        log.info("Request received for get messages with groupId: {}", groupId);
        return chatService.getChatMessagesForGroup(groupId, pageable);
    }

    public void broadcastMessage(String message) {
        // Broadcast message to all subscribers of '/user/topic/broadcast'
        simpMessagingTemplate.convertAndSend("/user/topic/broadcast", message);
    }
}
