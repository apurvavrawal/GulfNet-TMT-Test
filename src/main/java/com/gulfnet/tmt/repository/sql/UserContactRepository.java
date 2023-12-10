package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.UserContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserContactRepository extends JpaRepository<UserContact, UUID> {

    List<UserContact> findByUserId(UUID userId);

    List<UserContact> findByUserIdAndUserContactIdIn(UUID userId, List<UUID> userContactId);
}
