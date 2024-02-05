package com.gulfnet.tmt.repository.nosql;

import com.gulfnet.tmt.entity.nosql.Attachment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends MongoRepository<Attachment,Integer> {

}

