package com.gulfnet.tmt.dao;

import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.model.response.GroupResponse;
import com.gulfnet.tmt.repository.sql.GroupRepository;
import com.gulfnet.tmt.util.enums.Status;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

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

    public Optional<Group> findById(UUID groupId) {
        return groupRepository.findById(groupId);
    }

    public Page<GroupResponse> findAllBySearch(String search, Pageable pageable) {
        if(StringUtils.isEmpty(search)){
            return groupRepository.findAllGroups(pageable);
        }
        return groupRepository.findAllGroupsBySearch(search.toLowerCase(), pageable);
    }
    public void updateGroupStatusById(String status, UUID id) {
        groupRepository.updateGroupStatusById(status, id);
    }

    public void deleteById(UUID id){
        groupRepository.deleteById(id);
    }

}
