package com.gulfnet.tmt.repository.nosql;

import com.gulfnet.tmt.entity.nosql.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    List<Chat> findByChatId(String s);
}
