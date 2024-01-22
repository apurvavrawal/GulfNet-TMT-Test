package com.gulfnet.tmt.controller;


import com.gulfnet.tmt.model.response.AttachmentResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.service.AttachmentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@Slf4j
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/upload")
    @Operation(summary = "Upload and store a new file")
    public ResponseDto<List<AttachmentResponse>> uploadFiles(@RequestParam("files") List<MultipartFile> files, @RequestParam String attachmentType) {
        return attachmentService.uploadFiles(files, attachmentType);
    }

    @GetMapping("/download")
    @Operation(summary = "Download a file by file path")
    public ResponseEntity<?> downloadFile(@RequestParam("filePath") String filePath) throws FileNotFoundException {
        try {
            String decodedFilePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
            Resource resource = attachmentService.loadFileAsResource(decodedFilePath);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("Error downloading file: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

}

