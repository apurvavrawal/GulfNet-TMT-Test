package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.NamedQueries;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {

    Optional<Group> findByCode(String code);

    Optional<Group> findByName(String name);

    @Query("FROM Group g WHERE LOWER(g.name) like %:search% Or LOWER(g.code) like %:search%")
    Page<Group> findAllGroupsBySearch(String search, Pageable pageable);
}
