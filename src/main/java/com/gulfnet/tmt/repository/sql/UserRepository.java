package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.User;
import com.gulfnet.tmt.model.response.UserBasicInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUserNameAndPasswordAndAppType(String userName, String password, String appType);

    Optional<User> findByUserName(String userName);

    Page<User> findAll(Specification<User> specification, Pageable pageable);

    @Query("SELECT new com.gulfnet.tmt.model.response.UserBasicInfoResponse( u.id, u.userName, u.firstName, u.lastName, u.phone, u.email, u.profilePhoto, u.status) FROM User u " +
            " LEFT JOIN UserGroup ug ON u.id = ug.user.id " +
            " LEFT JOIN Group og ON ug.group.id = og.id " +
            "WHERE ug.group.id = :groupId AND u.status = :status")
    Page<UserBasicInfoResponse> findActiveUserOfGroup(@Param("status") String status, @Param("groupId") UUID groupId, Pageable pageable);

    @Query("SELECT COUNT(u.id), u.appType FROM User u GROUP BY u.appType")
    List<Object[]> countUsersByAppType();

    @Query( "SELECT COUNT(gu.id), ar.name " +
            "FROM User gu " +
            "LEFT JOIN UserRole ur ON gu.id = ur.user.id " +
            "LEFT JOIN AppRole ar ON ar.id = ur.role.id " +
            "GROUP BY ur.role.id, ar.name")
    List<Object[]> countMobileUsersByRole();

    @Query("SELECT count(gu.id), ar.name, og.name "+
            "FROM User gu "+
            "LEFT JOIN UserRole ur on gu.id = ur.user.id "+
            "LEFT JOIN UserGroup ug on gu.id = ug.user.id "+
            "LEFT JOIN AppRole ar on ar.id = ur.role.id "+
            "LEFT JOIN Group og on og.id = ug.group.id "+
            "GROUP BY ur.role.id, ar.name, og.name "+
            "ORDER BY  og.name, ar.name")
    List<Object[]> countMobileUsersByGroup();

}
