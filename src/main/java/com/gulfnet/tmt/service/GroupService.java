package com.gulfnet.tmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.GroupDao;
import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.GroupRequest;
import com.gulfnet.tmt.model.response.GroupResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.validator.GroupValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupValidator groupValidator;
    private final GroupDao groupDao;
    private final ObjectMapper mapper;
    public ResponseDto<GroupResponse> saveGroup(GroupRequest groupRequest) {
        groupValidator.validation(groupRequest, Optional.empty());
        Group group = mapper.convertValue(groupRequest, Group.class);
        group = groupDao.saveGroup(group);
        GroupResponse groupResponse = mapper.convertValue(group, GroupResponse.class);
        return ResponseDto.<GroupResponse>builder()
                .data(List.of(groupResponse))
                .build();
    }

    public ResponseDto<GroupResponse> updateGroup(UUID groupId, GroupRequest groupRequest) {
        Group groupDB = groupDao.findById(groupId).orElseThrow(
                () -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Group")));

        groupValidator.validation(groupRequest, Optional.of(groupId));
        Group group = mapper.convertValue(groupRequest, Group.class);
        group.setId(groupId);
        group.setCreatedBy(groupDB.getCreatedBy());
        group.setDateCreated(groupDB.getDateCreated());

        group = groupDao.saveGroup(group);
        GroupResponse groupResponse = mapper.convertValue(group, GroupResponse.class);

        return ResponseDto.<GroupResponse>builder()
                .data(List.of(groupResponse))
                .build();
    }
}
