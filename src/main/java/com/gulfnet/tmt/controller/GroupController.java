package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.request.GroupRequest;
import com.gulfnet.tmt.model.response.GroupResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.GroupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;

    @PostMapping
    public ResponseDto<GroupResponse> saveGroup(@RequestBody GroupRequest groupRequest) {
        log.info("Received request for group {}", groupRequest);
        return groupService.saveGroup(groupRequest);
    }

    @PutMapping("/{id}")
    public ResponseDto<GroupResponse> updateGroup(@PathVariable UUID id, @RequestBody GroupRequest groupRequest) {
        log.info("Received request for group {}", groupRequest);
        return groupService.updateGroup(id, groupRequest);
    }
}
