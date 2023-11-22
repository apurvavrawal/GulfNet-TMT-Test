package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.repository.sql.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GroupDao {

    private final GroupRepository groupRepository;
    public Group saveGroup(Group group) {
        return groupRepository.save(group);
    }

    public Optional<Group> findByCode(String code) {
        return groupRepository.findByCode(code);
    }

    public Optional<Group> findByName(String name) {
        return groupRepository.findByName(name);
    }
}
