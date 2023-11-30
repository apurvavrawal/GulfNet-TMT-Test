package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {

    UserGroup findByUserIdAndGroupId(UUID userId, UUID groupId);

}
