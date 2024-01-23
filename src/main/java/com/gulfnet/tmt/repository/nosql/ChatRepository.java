package com.gulfnet.tmt.repository.nosql;

import com.gulfnet.tmt.entity.nosql.Chat;
import com.gulfnet.tmt.model.response.ChatResponse;
import com.gulfnet.tmt.model.response.GroupChatResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    Page<ChatResponse> findByConversationId(String conversationId, Pageable pageable);

    Page<GroupChatResponse> findByReceiverId(String groupId, Pageable pageable);

    Chat findFirstByConversationIdOrderByDateCreatedDesc(String conversationId);

    Chat findByReceiverId(String receiverId);
}
