package com.gulfnet.tmt.repository.nosql;

import com.gulfnet.tmt.entity.nosql.Conversation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends MongoRepository<Conversation, String> {
}
