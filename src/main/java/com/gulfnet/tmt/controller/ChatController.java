package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.entity.nosql.ChatNotification;
import com.gulfnet.tmt.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

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

    @GetMapping("/messages/{senderId}/{receiverId}")
    public ResponseEntity<List<Chat>> findChatMessages(@PathVariable("senderId") String senderId, @PathVariable("receiverId") String receiverId){
        return ResponseEntity.ok(chatService.findChatMessages(senderId,receiverId));
    }


}
