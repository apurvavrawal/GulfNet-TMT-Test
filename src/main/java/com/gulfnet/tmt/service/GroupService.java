package com.gulfnet.tmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.GroupDao;
import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.GroupRequest;
import com.gulfnet.tmt.model.response.UserBasicInfoResponse;
import com.gulfnet.tmt.model.response.GroupResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.enums.ResponseMessage;
import com.gulfnet.tmt.util.enums.Status;
import com.gulfnet.tmt.validator.GroupValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

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
            group.setStatus(Status.ACTIVE.getName());
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
        Group groupDB = groupDao.findById(groupId).orElseThrow(
                () -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Group")));

        groupValidator.validation(groupRequest, Optional.of(groupId));
        Group group = mapper.convertValue(groupRequest, Group.class);
        group.setIcon(groupDao.findById(groupId).get().getIcon());

        group.setId(groupId);
        group.setCreatedBy(groupDB.getCreatedBy());
        group.setDateCreated(groupDB.getDateCreated());

        group.setStatus(Status.ACTIVE.name());
        group = groupDao.saveGroup(group);
        GroupResponse groupResponse = mapper.convertValue(group, GroupResponse.class);

        return ResponseDto.<GroupResponse>builder()
                .data(List.of(groupResponse))
                .build();

    }

    public ResponseDto<GroupResponse> getGroup(UUID groupId) {
       // Group group = groupDao.findByIdAndStatusI(groupId,Status.ACTIVE.getName());

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

    public ResponseDto<UserBasicInfoResponse> getGroupUsers(UUID id, Pageable pageable) {
        Page<UserBasicInfoResponse> groupPostResponses = userDao.findGroupPostResponseByIdIn(id, pageable);
        List<UserBasicInfoResponse> groupUserResponseList = new ArrayList<>();
        for (UserBasicInfoResponse groupUserResponse : groupPostResponses) {
            groupUserResponseList.add(groupUserResponse);
        }
        return ResponseDto.<UserBasicInfoResponse>builder()
                .data(groupUserResponseList)
                .count(groupPostResponses.stream().count())
                .total(groupPostResponses.getTotalElements())
                .build();

    }
    public String deleteGroupById(UUID id) {

        Group group = groupDao.findById(id).orElseThrow(
                () -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Group")));
        if (ObjectUtils.isEmpty(group)) {
            return ResponseMessage.GROUP_NOT_FOUND.getName();
        } else {
            groupDao.updateGroupStatusById(Status.INACTIVE.getName(), id);
           // groupDao.deleteById(id);
            return ResponseMessage.GROUP_DELETE_SUCCESSFULLY.getName();
        }
    }
}
