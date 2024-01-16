package com.gulfnet.tmt.repository.nosql;

import com.gulfnet.tmt.entity.nosql.ConversationList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationListRepository extends MongoRepository<ConversationList, String> {

}
