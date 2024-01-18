package com.gulfnet.tmt.controller;

import com.gulfnet.tmt.entity.sql.Attachment;
import com.gulfnet.tmt.model.response.AttachmentResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.repository.sql.AttachmentRepository;
import com.gulfnet.tmt.service.AttachmentService;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@RestController
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/upload")
    public ResponseDto<AttachmentResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        return attachmentService.uploadFile(file);
    }

    @PostMapping("/uploadfiles")
    public ResponseDto<List<AttachmentResponse>> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
      return attachmentService.uploadMultipleFiles(files);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Object> downloadFile(@PathVariable("fileName") String fileName) {
        return attachmentService.downloadFile(fileName);
    }
}