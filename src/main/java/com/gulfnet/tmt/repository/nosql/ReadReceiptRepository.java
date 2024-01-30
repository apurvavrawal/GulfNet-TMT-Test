package com.gulfnet.tmt.repository.nosql;

import com.gulfnet.tmt.entity.nosql.ReadReceipt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadReceiptRepository extends MongoRepository<ReadReceipt, String> {
    List<ReadReceipt> findByConsumerIdAndConversationId(String userId, String conversationId);

    List<ReadReceipt> findByConsumerId(String userId);
}
