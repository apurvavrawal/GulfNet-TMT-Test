package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.nosql.Conversation;
import com.gulfnet.tmt.repository.nosql.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConversationDao {
    private final ConversationRepository conversationRepository;

    public Optional<Conversation> getConversationListByUserId(String userId) {
        return conversationRepository.findById(userId);
    }
}
