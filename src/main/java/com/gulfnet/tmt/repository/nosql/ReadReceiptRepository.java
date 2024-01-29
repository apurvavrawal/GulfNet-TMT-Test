package com.gulfnet.tmt.repository.nosql;

import com.gulfnet.tmt.entity.nosql.ReadReceipt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadReceiptRepository extends MongoRepository<ReadReceipt, String> {
    List<ReadReceipt> findByChatId(String ChatId);

    Optional<ReadReceipt> findByChatIdAndConsumerId(String chatId, String consumerId);

    List<ReadReceipt> findByConsumerId(String receiverId);
}
