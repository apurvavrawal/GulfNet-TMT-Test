package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {

    Optional<Group> findByCode(String code);

    Optional<Group> findByName(String name);
}
