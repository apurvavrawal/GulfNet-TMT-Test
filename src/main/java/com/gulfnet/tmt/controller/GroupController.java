package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.request.GroupRequest;
import com.gulfnet.tmt.model.response.GroupResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<GroupResponse> saveGroup(GroupRequest groupRequest) {
        log.info("Received request for group {}", groupRequest);
        return groupService.saveGroup(groupRequest);
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<GroupResponse> updateGroup(@PathVariable UUID id, GroupRequest groupRequest) {
        log.info("Received request for group {} of id {}", groupRequest, id);
        return groupService.updateGroup(id, groupRequest);
    }

    @GetMapping("/{id}")
    public ResponseDto<GroupResponse> getGroup(@PathVariable UUID id) {
        log.info("Received request for group id {}", id);
        return groupService.getGroup(id);
    }

    @GetMapping
    public ResponseDto<GroupResponse> getGroups(@RequestParam(value = "search", required = false) String search, @PageableDefault(sort = {"dateCreated"}, direction = Sort.Direction.DESC)Pageable pageable) {
        log.info("Received request for group id {}", search);
        return groupService.getAllGroups(search, pageable);
    }
}
