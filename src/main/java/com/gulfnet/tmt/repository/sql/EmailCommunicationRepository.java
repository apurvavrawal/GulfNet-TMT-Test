package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.EmailCommunication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailCommunicationRepository extends JpaRepository<EmailCommunication, UUID> {
}
