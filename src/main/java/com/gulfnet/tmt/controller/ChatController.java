package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.entity.nosql.ChatNotification;
import com.gulfnet.tmt.model.response.ChatResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.chatservices.ChatService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
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

    @Autowired
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("/chat")
    public void processMessage(@Payload Chat chatMessage){
        //   receiver/queue/messages
        Chat savedMsg = chatService.save(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiverId(), "/queue/messages", ChatNotification.builder()
                .id(savedMsg.getId())
                .senderId(savedMsg.getSenderId())
                .receiverId(savedMsg.getReceiverId())
                .content(savedMsg.getContent())
                .build());
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

    @MessageMapping("/chat/sendMessage")
    @SendTo("/user/queue/reply")
    public String processGroupMessage(MessageDTO messageDTO) {
        // Process the message here (e.g., save to database, modify, etc.)
        return "Processed message: " + messageDTO.content;
    }

//    @MessageMapping("/chat/privateMessage")
//    @SendToUser("/queue/reply")
//    public String processPrivateMessage(Principal principal, MessageDTO messageDTO) {
//
//        // You can obtain the username from the principal object
//        String username = principal.getName();
//        // Process the message here
//        return "Private message from " + username + ": " + messageDTO.content;
//    }

    @MessageMapping("/chat/privateMessage")
    public void processPrivateMessage(Principal principal, MessageDTO messageDTO) {
        logger.debug("Received message from principal: {}", principal);

        if (principal == null) {throw new IllegalStateException("Principal cannot be null");}
        String username = principal.getName(); // Sender's username

        String recipient = messageDTO.getRecipient(); // Recipient's username
        if (recipient == null) {throw new IllegalStateException("Recipient cannot be null");}

        // Construct the response message
        String responseMessage = "Private message from " + username + ": " + messageDTO.getContent();

        // Send the message to the recipient's queue
        simpMessagingTemplate.convertAndSendToUser(
                recipient,
                "/queue/reply",
                responseMessage
        );
    }


    public void broadcastMessage(String message) {
        // Broadcast message to all subscribers of '/user/topic/broadcast'
        simpMessagingTemplate.convertAndSend("/user/topic/broadcast", message);
    }

    @Setter
    @Getter
    public static class MessageDTO {
        private String content;
        private String recipient;
    }

}
