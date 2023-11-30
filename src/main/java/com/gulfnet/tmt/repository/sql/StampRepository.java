package com.gulfnet.tmt.repository.sql;

import com.gulfnet.tmt.entity.sql.Stamp;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface StampRepository extends JpaRepository<Stamp, UUID> {
    @Modifying
    @Transactional
    @Query("UPDATE Stamp s SET s.status = :status WHERE s.id = :id")
    void updateStampStatusById(@Param("status") String status, @Param("id") UUID id);

    Stamp findByIdAndStatus(UUID id, String status);

    Page<Stamp> findAllByStatus(String status, Pageable pageable);

    long count();
}