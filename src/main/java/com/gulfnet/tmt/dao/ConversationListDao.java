package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.nosql.ConversationList;
import com.gulfnet.tmt.repository.nosql.ConversationListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ConversationListDao {
    private final ConversationListRepository conversationListRepository;

    public Optional<ConversationList> getConversationListByUserId(String userId) {
        return conversationListRepository.findById(userId);
    }
}
