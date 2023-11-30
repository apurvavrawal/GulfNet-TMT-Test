package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.sql.Stamp;
import com.gulfnet.tmt.repository.sql.StampRepository;
import com.gulfnet.tmt.util.enums.Status;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class StampDao {
    private final StampRepository stampRepository;

    public Stamp saveStamp(Stamp stamp) {
        return stampRepository.save(stamp);
    }

    public Page<Stamp> findAll(Pageable pageable) {
        return stampRepository.findAllByStatus(Status.ACTIVE.getName(), pageable);
    }

    public Stamp findByIdAndStatusI(UUID id, String status) {
        return stampRepository.findByIdAndStatus(id, status);
    }

    public Optional<Stamp> findById(UUID id) {
        return stampRepository.findById(id);
    }

    public void updateStampStatusById(String status, UUID id) {
        stampRepository.updateStampStatusById(status, id);
    }

    public long getCount() {
       return stampRepository.count();
    }
}
