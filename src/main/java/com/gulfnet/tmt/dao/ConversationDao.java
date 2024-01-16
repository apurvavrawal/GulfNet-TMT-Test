package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.nosql.Conversation;
import com.gulfnet.tmt.entity.nosql.ConversationList;
import com.gulfnet.tmt.repository.nosql.ConversationListRepository;
import com.gulfnet.tmt.repository.nosql.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConversationDao {
    private final ConversationListRepository conversationListRepository;
    private final ConversationRepository conversationRepository;

    public Optional<ConversationList> getConversationListByUserId(String userId) {
        return conversationListRepository.findById(userId);
    }

    public Conversation createConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }
}
