package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.model.response.StampResponse;
import com.gulfnet.tmt.service.StampService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/settings/stamp")
public class StampController {

    private final StampService stampService;

    @GetMapping
    public ResponseDto<StampResponse> getStamps(@PageableDefault(sort = {"createdOn"}, direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Received stamp Get request for pageable{}", pageable);
        return stampService.getAllStamp(pageable);
    }

    @GetMapping("/{stampId}")
    public ResponseDto<StampResponse> getStamp(@PathVariable UUID stampId) {
        log.info("Received stamp Get request for stampId {}", stampId);
        return stampService.getStampById(stampId);
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseDto<StampResponse> saveStamp(@RequestParam(name = "stampFile") MultipartFile file) {
        log.info("Received stamp creation request for {}", file);
        return stampService.saveStamp(file);
    }

    @DeleteMapping("/{stampId}")
    public ResponseEntity<String> deleteStamp(@PathVariable UUID stampId) {
        log.info("Received stamp deletion request for stampId {}", stampId);
        return ResponseEntity.ok(stampService.deleteStampById(stampId));
    }
}
