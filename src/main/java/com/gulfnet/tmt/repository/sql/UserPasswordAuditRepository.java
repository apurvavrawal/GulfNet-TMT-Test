package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.UserPasswordAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserPasswordAuditRepository extends JpaRepository<UserPasswordAudit, UUID> {

    List<UserPasswordAudit> findByUserIdAndStatusAndAction(UUID userId, String status, String action);
}
