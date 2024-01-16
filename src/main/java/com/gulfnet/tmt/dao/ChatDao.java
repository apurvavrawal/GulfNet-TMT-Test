package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.model.response.ChatResponse;
import com.gulfnet.tmt.repository.nosql.ChatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatDao {

    private final ChatRepository chatRepository;


    public Optional<Chat> findMessageById(String chatId) {
        return chatRepository.findById(chatId);
    }

    public Page<ChatResponse> findChatMessagesById(String senderId, String receiverId, Pageable pageable) {
        return chatRepository.findAllBySenderIdAndReceiverId(senderId, receiverId, pageable);
    }

    public Chat save(Chat chatMessage) {
        return chatRepository.save(chatMessage);
    }
}
