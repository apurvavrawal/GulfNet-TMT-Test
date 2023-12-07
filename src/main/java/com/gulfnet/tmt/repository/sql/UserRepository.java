package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.model.response.GroupUserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUserNameAndPasswordAndAppType(String userName, String password, String appType);

    Optional<User> findByUserName(String userName);

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    @Query("SELECT new com.gulfnet.tmt.model.response.GroupUserResponse( u.id, u.userName, u.firstName, u.lastName, u.phone, u.email, u.profilePhoto) FROM User u " +
            " LEFT JOIN UserGroup ug ON u.id = ug.user.id " +
            " LEFT JOIN Group og ON ug.group.id = og.id " +
            "WHERE ug.group.id = :groupId AND u.status = :status")
    Page<GroupUserResponse> findActiveUserOfGroup(@Param("status") String status, @Param("groupId") UUID groupId, Pageable pageable);

}
