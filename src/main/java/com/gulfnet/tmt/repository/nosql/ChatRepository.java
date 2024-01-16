package com.gulfnet.tmt.repository.nosql;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.model.response.ChatResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    Page<ChatResponse> findAllByConversationId(String conversationId, Pageable pageable);
}
