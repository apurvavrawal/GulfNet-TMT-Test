package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {

    UserRole findByUserIdAndRoleId(UUID userId, UUID roleId);

}
