package com.gulfnet.tmt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.config.GulfNetTMTServiceConfig;
import com.gulfnet.tmt.entity.sql.Attachment;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.model.response.AttachmentResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.repository.sql.AttachmentRepository;
import com.gulfnet.tmt.util.ErrorConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final ObjectMapper mapper;
   // private static final String FOLDER_PATH = "C:\\Users\\JSPNLP-\\uploadfie\\";

    @Autowired
    private GulfNetTMTServiceConfig gulfNetTMTServiceConfig;

    @Autowired
    private AttachmentRepository attachmentRepository;

    public ResponseDto<List<AttachmentResponse>> uploadFiles(List<MultipartFile> files, String attachmentType) {
        List<AttachmentResponse> attachmentResponses = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                File currentFile = new File(gulfNetTMTServiceConfig.getLocalFilePath(),
                        System.currentTimeMillis() + "_" + attachmentType + "_" + file.getOriginalFilename());

                Attachment item = Attachment.builder()
                        .fileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .fileLocation(currentFile.getAbsolutePath())
                        .attachmentType(attachmentType)
                        .build();

                Attachment attachment = attachmentRepository.save(item);

                file.transferTo(currentFile.toPath());
                AttachmentResponse response = mapper.convertValue(attachment, AttachmentResponse.class);
                attachmentResponses.add(response);
            } catch (IOException e) {
                throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, e.getMessage());
            }
        }

        return ResponseDto.<List<AttachmentResponse>>builder()
                .status(0)
                .data(List.of(attachmentResponses))
                .message("Files uploaded successfully")
                .build();
    }

    public ResponseEntity<Object> downloadFile(String fileName) {
        try {
            Optional<Attachment> fileItem = attachmentRepository.findByFileName(fileName);
            if (fileItem.isPresent()) {
                Attachment item = fileItem.get();
                String path = item.getFileLocation();
                byte[] content = Files.readAllBytes(Path.of(path));
                return ResponseEntity.status(HttpStatus.FOUND)
                        .contentType(MediaType.valueOf(item.getFileType())).body(content);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File not found for fileName " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to read file content.");
        }
    }
}

