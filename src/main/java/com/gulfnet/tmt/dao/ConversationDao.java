package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.nosql.Conversation;
import com.gulfnet.tmt.model.response.ConversationListResponse;
import com.gulfnet.tmt.repository.nosql.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConversationDao {

    private final ConversationRepository conversationRepository;

    public List<Conversation> getConversationListForPrivate(String userId) {
        return conversationRepository.findBySenderIdOrConsumerId(userId, userId);
    }

    public Conversation createConversation(Conversation conversation) {
        return conversationRepository.save(conversation);
    }
}
