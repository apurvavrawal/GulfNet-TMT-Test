package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.request.GroupRequest;
import com.gulfnet.tmt.model.response.UserBasicInfoResponse;
import com.gulfnet.tmt.model.response.GroupResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "Create and save a new group")
    public ResponseDto<GroupResponse> saveGroup(GroupRequest groupRequest) {
        log.info("Received request for group {}", groupRequest);
        return groupService.saveGroup(groupRequest);
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = " Modify details of an existing group by ID.")
    public ResponseDto<GroupResponse> updateGroup(@PathVariable UUID id, GroupRequest groupRequest) {
        log.info("Received request for group {} of id {}", groupRequest, id);
        return groupService.updateGroup(id, groupRequest);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve details of a specific group by ID.")
    public ResponseDto<GroupResponse> getGroup(@PathVariable UUID id) {
        log.info("Received request for group id {}", id);
        return groupService.getGroup(id);
    }

    @GetMapping
    @Operation(summary = "Retrieve groups with optional search and pagination.")
    public ResponseDto<GroupResponse> getGroups(@RequestParam(value = "search", required = false) String search, @PageableDefault(sort = {"dateCreated"}, direction = Sort.Direction.DESC)Pageable pageable) {
        log.info("Received request for group id {}", search);
        return groupService.getAllGroups(search, pageable);
    }

    @GetMapping("/{groupId}/users")
    @Operation(summary = "Retrieve basic information of users within a group.")
    public ResponseDto<UserBasicInfoResponse> getGroupUsers(@PathVariable UUID groupId, @PageableDefault(sort = {"dateCreated"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return groupService.getGroupUsers(groupId, pageable);
    }

    @DeleteMapping("/{groupId}")
    @Operation(summary = "Remove a group by its unique ID.")
    public ResponseDto<GroupResponse> deleteGroup(@PathVariable UUID groupId) {
        log.info("Received group deletion request for groupId {}", groupId);
        return groupService.deleteGroupById(groupId);
    }
}
