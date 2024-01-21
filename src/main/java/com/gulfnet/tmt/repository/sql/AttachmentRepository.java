package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachmentRepository extends MongoRepository<Attachment,Integer> {

   // Optional<Attachment> findByFileId(Integer id);

    Optional<Attachment> findByFileName(String fileName);
}

