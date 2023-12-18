package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.LoginAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoginAuditRepository extends JpaRepository<LoginAudit, UUID> {
    Optional<LoginAudit> findByUserId(UUID id);

    @Modifying
    @Transactional
    @Query("UPDATE LoginAudit l SET l.loginExpiryDate = :expiryTime, l.machineInfo = :machineInfo WHERE l.userId = :id")
    void updateUserIdAndMachineInfo(@Param("id") UUID id,@Param("machineInfo") String machineInfo, @Param("expiryTime") Date expiryTime);
}
