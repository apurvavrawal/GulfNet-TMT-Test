package com.gulfnet.tmt.service;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.repository.nosql.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomService chatRoomService;

    public Chat save(Chat chatMessage){
        var chatId = chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getReceiverId(),true).orElseThrow();
        chatMessage.setChatId(chatId);
        chatRepository.save(chatMessage);
        return chatMessage;
    }

    public List<Chat> findChatMessages(String senderId, String receiverId){
        var chatId = chatRoomService.getChatRoomId(senderId, receiverId,false);
        return chatId.map(chatRepository::findByChatId).orElse(new ArrayList<>());
    }
}

