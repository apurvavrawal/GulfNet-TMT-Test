package com.gulfnet.tmt.repository.nosql;

import com.gulfnet.tmt.entity.nosql.UserDetail;
import com.gulfnet.tmt.util.enums.ChatStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailRepository extends MongoRepository<UserDetail, String> {
    List<UserDetail> findAllByChatStatus(ChatStatus chatStatus);
}
