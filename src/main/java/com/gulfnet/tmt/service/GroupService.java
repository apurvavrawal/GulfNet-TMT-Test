package com.gulfnet.tmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.GroupDao;
import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.GroupRequest;
import com.gulfnet.tmt.model.response.GroupUserResponse;
import com.gulfnet.tmt.model.response.GroupResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.validator.GroupValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupValidator groupValidator;
    private final GroupDao groupDao;
    private final FileStorageService fileStorageService;
    private final ObjectMapper mapper;
    private final UserDao userDao;

    public ResponseDto<GroupResponse> saveGroup(GroupRequest groupRequest) {
        try {
            groupValidator.validation(groupRequest, Optional.empty());
            Group group = mapper.convertValue(groupRequest, Group.class);
            group.setIcon(fileStorageService.uploadFile(groupRequest.getIcon(), "group"));
            group = groupDao.saveGroup(group);
            GroupResponse groupResponse = mapper.convertValue(group, GroupResponse.class);
            return ResponseDto.<GroupResponse>builder()
                    .data(List.of(groupResponse))
                    .build();

        } catch (IOException e) {
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, e.getMessage());
        }
    }

    public ResponseDto<GroupResponse> updateGroup(UUID groupId, GroupRequest groupRequest) {
        try {
            Group groupDB = groupDao.findById(groupId).orElseThrow(
                    () -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Group")));

            groupValidator.validation(groupRequest, Optional.of(groupId));
            Group group = mapper.convertValue(groupRequest, Group.class);
            group.setIcon(fileStorageService.uploadFile(groupRequest.getIcon(), "group"));
            group.setId(groupId);
            group.setCreatedBy(groupDB.getCreatedBy());
            group.setDateCreated(groupDB.getDateCreated());

            group = groupDao.saveGroup(group);
            GroupResponse groupResponse = mapper.convertValue(group, GroupResponse.class);

            return ResponseDto.<GroupResponse>builder()
                    .data(List.of(groupResponse))
                    .build();

        } catch (IOException e) {
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, e.getMessage());
        }
    }

    public ResponseDto<GroupResponse> getGroup(UUID groupId) {
        Group group = groupDao.findById(groupId).orElseThrow(
                () -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Group")));
        GroupResponse groupResponse = mapper.convertValue(group, GroupResponse.class);
        return ResponseDto.<GroupResponse>builder()
                .data(List.of(groupResponse))
                .build();
    }

    public ResponseDto<GroupResponse> getAllGroups(String search, Pageable pageable) {
        Page<GroupResponse> groups = groupDao.findAllBySearch(search, pageable);
        return ResponseDto.<GroupResponse>builder()
                .data(groups.getContent())
                .total(groups.getTotalElements())
                .count(groups.stream().count())
                .build();
    }

    public ResponseDto<GroupUserResponse> getGroupUsers(UUID id, Pageable pageable) {
        Page<GroupUserResponse> groupPostResponses = userDao.findGroupPostResponseByIdIn(id, pageable);
        List<GroupUserResponse> groupUserResponseList = new ArrayList<>();
        for (GroupUserResponse groupUserResponse : groupPostResponses) {
            groupUserResponseList.add(groupUserResponse);
        }
        return ResponseDto.<GroupUserResponse>builder()
                .data(groupUserResponseList)
                .count(groupPostResponses.stream().count())
                .total(groupPostResponses.getTotalElements())
                .build();

    }
}
