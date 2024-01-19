package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.entity.sql.Stamp;
import com.gulfnet.tmt.model.response.GroupResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {

    Optional<Group> findByCode(String code);

    Optional<Group> findByName(String name);

    @Query("SELECT new com.gulfnet.tmt.model.response.GroupResponse( OG.id, OG.code, OG.name, OG.type, OG.icon, COUNT(GTU.ID) as userCount, OG.dateCreated, OG.dateUpdated, OG.createdBy, OG.updatedBy, OG.status)"+
            " FROM Group OG " +
            " LEFT JOIN UserGroup UG ON UG.group.id = OG.ID " +
            " LEFT JOIN User GTU ON GTU.ID = UG.user.id " +
            " WHERE (LOWER(OG.name) like %:search% Or LOWER(OG.code) like %:search%) AND OG.status = :status "+
            " GROUP BY OG.ID")
    Page<GroupResponse> findAllGroupsBySearch(@Param("status") String status,String search, Pageable pageable);

    @Query("SELECT new com.gulfnet.tmt.model.response.GroupResponse( OG.ID, OG.code, OG.name, OG.type, OG.icon, COUNT(GTU.ID) as userCount, OG.dateCreated, OG.dateUpdated, OG.createdBy, OG.updatedBy, OG.status)"+
            " FROM Group OG " +
            " LEFT JOIN UserGroup UG ON UG.group.id = OG.ID " +
            " LEFT JOIN User GTU ON GTU.ID = UG.user.id " +
            " WHERE  OG.status = :status" +
            " GROUP BY OG.ID")
    Page<GroupResponse> findAllGroups(@Param("status") String status,Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Group g SET g.status = :status WHERE g.id = :id")
    void updateGroupStatusById(@Param("status") String status, @Param("id") UUID id);


}
