package com.gulfnet.tmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.dao.GroupDao;
import com.gulfnet.tmt.entity.sql.Group;
import com.gulfnet.tmt.model.request.GroupRequest;
import com.gulfnet.tmt.model.response.GroupResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.validator.GroupValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupValidator groupValidator;
    private final GroupDao groupDao;
    private final ObjectMapper mapper;
    public ResponseDto<GroupResponse> saveGroup(GroupRequest groupRequest) {
        groupValidator.validation(groupRequest);
        Group group = mapper.convertValue(groupRequest, Group.class);
        group = groupDao.saveGroup(group);
        GroupResponse groupResponse = mapper.convertValue(group, GroupResponse.class);
        return ResponseDto.<GroupResponse>builder()
                .data(List.of(groupResponse))
                .build();
    }
}
