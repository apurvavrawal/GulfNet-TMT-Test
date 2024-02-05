package com.gulfnet.tmt.repository.nosql;

import com.gulfnet.tmt.entity.nosql.Conversation;
import com.gulfnet.tmt.util.enums.ConversationType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
    Conversation findBySenderIdAndConsumerId(String senderId, String receiverId);

    List<Conversation> findBySenderIdOrConsumerId(String senderId, String consumerId);

    Conversation findBySenderIdAndConsumerIdAndConversationType(String senderId, String consumerId, ConversationType conversationType);

    Conversation findByConsumerId(String consumerId);

    Conversation findByConsumerIdAndConversationType(String consumerId, ConversationType conversationType);
}
