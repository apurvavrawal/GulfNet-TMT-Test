package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, UUID> {

    List<AppRole> findAllByCodeIn(List<String> codes);
}
