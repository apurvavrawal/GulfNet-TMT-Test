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
    GulfNetTMTServiceConfig gulfNetTMTServiceConfig;

    @Autowired
    private AttachmentRepository attachmentRepository;

    public ResponseDto<AttachmentResponse> uploadFile(MultipartFile file) {
        String path = null;
        AttachmentResponse attachmentResponse = null;
        try {

            path = gulfNetTMTServiceConfig.getLocalFileUploadPath()+file.getOriginalFilename();
           // path = FOLDER_PATH + file.getOriginalFilename();
            Attachment item = Attachment.builder()
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileLocation(path)
                    .build();

            Attachment attachment = attachmentRepository.save(item);
            file.transferTo(new File(path).toPath());
            attachmentResponse = mapper.convertValue(attachment, AttachmentResponse.class);
        } catch (IOException e) {
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, e.getMessage());
        }
        return ResponseDto.<AttachmentResponse>builder()
                .status(0)
                .data(List.of(attachmentResponse))
                .message("File upload sucessfully")
                .build();
    }

    public ResponseDto<List<AttachmentResponse>> uploadMultipleFiles(List<MultipartFile> files) {
        List<AttachmentResponse> attachmentResponses = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                String fileName = file.getOriginalFilename();
                String filePath = gulfNetTMTServiceConfig.getLocalFileUploadPath() + File.separator + fileName;

                Attachment item = Attachment.builder()
                        .fileName(fileName)
                        .fileType(file.getContentType())
                        .fileLocation(filePath)
                        .build();

                Attachment savedAttachment = attachmentRepository.save(item);
                file.transferTo(new File(filePath).toPath());

                AttachmentResponse attachmentResponse = mapper.convertValue(savedAttachment, AttachmentResponse.class);
                attachmentResponses.add(attachmentResponse);
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

