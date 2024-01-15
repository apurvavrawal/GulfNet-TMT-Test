package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.model.response.ChatResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.chatservices.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    // Process the one-to-one message and save to DataBase
    @MessageMapping("/chat/privateMessage")
    public void processPrivateMessage(Principal principal, ChatResponse chatResponse) {
        logger.debug("Received message from principal: {}", principal);

        if (principal == null) {throw new IllegalStateException("Principal cannot be null");}
        String recipient = chatResponse.getReceiverName(); // Recipient's username
        if (recipient == null) {throw new IllegalStateException("Recipient cannot be null");}
        String responseMessage = chatResponse.getContent();
        // Send the message to the recipient's queue

        Chat savedMsg = chatService.save(chatResponse);
        log.info("Message processing with following metadata: {}", savedMsg);
        simpMessagingTemplate.convertAndSendToUser(recipient, "/queue/reply", responseMessage);
    }

    // Process the group message and save to DataBase
    @MessageMapping("/chat/sendMessage")
    @SendTo("/user/queue/reply")
    public String processGroupMessage(ChatResponse chatResponse) {
        log.info("Request received for processing Message with Id: {}", chatResponse.getId());
        return chatResponse.getContent();
    }

    // Returns List of Chats between sender and receiver by their Id
    @GetMapping("/messages/{senderId}/{receiverId}")
    public ResponseDto<ChatResponse> getChatMessages(@PathVariable("senderId") String senderId,
                                                            @PathVariable("receiverId") String receiverId,
                                                            @PageableDefault(sort = {"dateCreated"}, direction = Sort.Direction.DESC)Pageable pageable){
        log.info("Request received for get message with senderId: {} and receiverId: {}", senderId, receiverId);
        return chatService.getChatMessages(senderId, receiverId, pageable);
    }

    // Returns message details for requested chatId
    @GetMapping("/messages/{chatId}")
    public ResponseDto<ChatResponse> getMessageById(@PathVariable String chatId) {
        log.info("Request received for get message for chatId: {}", chatId);
        return chatService.getMessageById(chatId);
    }

    public void broadcastMessage(String message) {
        // Broadcast message to all subscribers of '/user/topic/broadcast'
        simpMessagingTemplate.convertAndSend("/user/topic/broadcast", message);
    }
}
