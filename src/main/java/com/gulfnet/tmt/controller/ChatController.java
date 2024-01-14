package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.entity.nosql.ChatNotification;
import com.gulfnet.tmt.model.response.ChatResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.ChatRoomService;
import com.gulfnet.tmt.service.chatservices.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;
    private final ChatRoomService chatRoomService;

    @MessageMapping("/chat")
    public void processMessage(@Payload Chat chatMessage){
        //   receiver/queue/messages
        var chatId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(), true);
        chatMessage.setChatId(chatId.get());

        Chat savedMsg = chatService.save(chatMessage);
        simpMessagingTemplate.convertAndSendToUser(chatMessage.getReceiverId(), "/queue/messages",
                new ChatNotification(
                        savedMsg.getId(),
                        savedMsg.getSenderId(),
                        savedMsg.getReceiverId(),
                        savedMsg.getContent()));
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

}
