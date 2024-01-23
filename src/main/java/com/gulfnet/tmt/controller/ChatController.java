package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.model.response.ChatResponse;
import com.gulfnet.tmt.model.response.GroupChatResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.chatservices.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    // Process the one-to-one message and save to DataBase
    @MessageMapping("/chat/privateMessage")
    public void processPrivateMessage(Principal principal, Chat chat) {
        logger.debug("Received message from principal: {}", principal);

        if (principal == null) {throw new IllegalStateException("Principal cannot be null");}
        String receiverId = chat.getReceiverId();
        if (receiverId == null) {throw new IllegalStateException("Recipient cannot be null");}
        // Send the message to the recipient's queue
        Chat savedMsg = chatService.savePrivateMessage(chat);
        String conversationId = savedMsg.getConversationId();
        log.info("Message processing for private chat with following metadata: {}", savedMsg);
        simpMessagingTemplate.convertAndSendToUser(receiverId+conversationId,"/queue/reply", savedMsg);
    }

    // Process the group message and save to DataBase
    @MessageMapping("/chat/groupMessage")
    public void processGroupMessage(Chat chat) {
        logger.debug("Received group message");

        String groupId = chat.getReceiverId();
        if (groupId == null) {
            throw new IllegalStateException("Group ID cannot be null");
        }
        Chat savedMsg = chatService.saveGroupMessage(chat);
        log.info("Message processing for group chat with following metadata: {}", savedMsg);

        simpMessagingTemplate.convertAndSendToUser(groupId,"/queue/reply", savedMsg);
    }

    // Returns List of Chats(private) between sender and receiver by conversationId
    @GetMapping("/messages/history/private-chat/{conversationId}")
    public ResponseDto<ChatResponse> getMessageHistoryForPrivateChat(@PathVariable("conversationId") String conversationId,
                                                            @PageableDefault(sort = {"dateCreated"}, direction = Sort.Direction.DESC)Pageable pageable){
        log.info("Request received for get messages with conversationId: {}", conversationId);
        return chatService.getChatMessagesForPrivate(conversationId, pageable);
    }

    // Returns List of Chats in group by groupId
    @GetMapping("/messages/history/group-chat/{groupId}")
    public ResponseDto<GroupChatResponse> getMessageHistoryForGroupChat(@PathVariable("groupId") String groupId,
                                                                        @PageableDefault(sort = {"dateCreated"}, direction = Sort.Direction.ASC, size = 50, value = 50)Pageable pageable){
        log.info("Request received for get messages with groupId: {}", groupId);
        return chatService.getChatMessagesForGroup(groupId, pageable);
    }

    // Returns message details for requested chatId
    @GetMapping("/messages/{chatId}")
    public ResponseDto<ChatResponse> getMessageById(@PathVariable("chatId") String chatId) {
        log.info("Request received for get message for chatId: {}", chatId);
        return chatService.getMessageById(chatId);
    }

    public void broadcastMessage(String message) {
        // Broadcast message to all subscribers of '/user/topic/broadcast'
        simpMessagingTemplate.convertAndSend("/user/topic/broadcast", message);
    }
}
