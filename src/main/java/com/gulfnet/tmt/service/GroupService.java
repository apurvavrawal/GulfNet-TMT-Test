package com.gulfnet.tmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.GroupDao;
import com.gulfnet.tmt.dao.UserDao;
import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.exceptions.ValidationException;
import com.gulfnet.tmt.model.request.ConversationRequest;
import com.gulfnet.tmt.model.request.GroupRequest;
import com.gulfnet.tmt.model.response.*;
import com.gulfnet.tmt.service.chatservices.impl.ConversationServiceImpl;
import com.gulfnet.tmt.util.ErrorConstants;
import com.gulfnet.tmt.util.enums.ConversationType;
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
    private final ConversationServiceImpl conversationServiceImpl;
    private final String PATH = "/avatar/group";

    public ResponseDto<GroupResponse> saveGroup(GroupRequest groupRequest) {
        try {
            groupValidator.validation(groupRequest, Optional.empty());
            Group group = mapper.convertValue(groupRequest, Group.class);
            group.setIcon(fileStorageService.uploadFile(groupRequest.getIcon(), "group", PATH));
            group.setStatus(Status.ACTIVE.getName());
            group = groupDao.saveGroup(group);
            GroupResponse groupResponse = mapper.convertValue(group, GroupResponse.class);

            // Adding new conversation entry for assigned groups for chat
            ConversationRequest conversationRequest = new ConversationRequest();
            conversationRequest.setSenderId(groupResponse.getCreatedBy());
            conversationRequest.setConsumerId(String.valueOf(group.getId()));
            conversationRequest.setConversationType(ConversationType.GROUP);

            conversationServiceImpl.createConversationForGroup(conversationRequest);

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

            if(groupRequest.getIcon() != null) {
                group.setIcon(fileStorageService.uploadFile(groupRequest.getIcon(), "group",PATH));
            }
            else{
                group.setIcon(groupDao.findById(groupId).get().getIcon());
            }

            group.setId(groupId);
            group.setCreatedBy(groupDB.getCreatedBy());
            group.setDateCreated(groupDB.getDateCreated());

        group.setStatus(Status.ACTIVE.name());
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

        // Adding new conversation entry for assigned groups for chat

        for(GroupResponse groupResponse: groups.getContent()){
            ConversationRequest conversationRequest = new ConversationRequest();
            conversationRequest.setSenderId(groupResponse.getCreatedBy());
            conversationRequest.setConsumerId(String.valueOf(groupResponse.getId()));
            conversationRequest.setConversationType(ConversationType.GROUP);
            conversationServiceImpl.createConversationForGroup(conversationRequest);
        }
        return ResponseDto.<GroupResponse>builder()
                .data(groups.getContent())
                .total(groups.getTotalElements())
                .count(groups.stream().count())
                .build();
    }

    public ResponseDto<UserBasicInfoResponse> getGroupUsers(UUID id,String searchTerm, Pageable pageable) {
        Page<UserBasicInfoResponse> groupPostResponses = userDao.findGroupPostResponseByIdIn(id,searchTerm ,pageable);
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
    public  ResponseDto<GroupResponse> deleteGroupById(UUID id) {
        Group group = groupDao.findById(id).orElseThrow(
                () -> new ValidationException(ErrorConstants.NOT_FOUND_ERROR_CODE, MessageFormat.format(ErrorConstants.NOT_FOUND_ERROR_MESSAGE, "Group")));
        if (ObjectUtils.isEmpty(group)) {
            return ResponseDto.<GroupResponse>builder().data(null).build();
        } else {
            groupDao.updateGroupStatusById(Status.INACTIVE.getName(), id);
            return ResponseDto.<GroupResponse>builder()
                    .status(0)
                    .message("Group deleted successfully")
                    .build();
        }
    }
}
