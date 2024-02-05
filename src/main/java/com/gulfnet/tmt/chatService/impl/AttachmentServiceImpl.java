package com.gulfnet.tmt.chatService.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gulfnet.tmt.chatService.AttachmentService;
import com.gulfnet.tmt.config.GulfNetTMTServiceConfig;
import com.gulfnet.tmt.entity.nosql.Attachment;
import com.gulfnet.tmt.exceptions.GulfNetTMTException;
import com.gulfnet.tmt.model.response.AttachmentResponse;
import com.gulfnet.tmt.model.response.ResponseDto;
import com.gulfnet.tmt.repository.nosql.AttachmentRepository;
import com.gulfnet.tmt.util.ErrorConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private GulfNetTMTServiceConfig gulfNetTMTServiceConfig;
    @Autowired
    private AttachmentRepository attachmentRepository;
    private final String IMAGE = "images";
    private final String VIDEO = "videos";
    private final String AUDIO = "audio";
    private final String DOCUMENT = "documents";

    public ResponseDto<List<AttachmentResponse>> uploadFiles(List<MultipartFile> files, String attachmentType) {
        List<AttachmentResponse> attachmentResponses = new ArrayList<>();
        String fileFolder = "/attachment/";

        if(attachmentType.equalsIgnoreCase(IMAGE)) fileFolder += IMAGE;
        else if(attachmentType.equalsIgnoreCase(AUDIO)) fileFolder += AUDIO;
        else if(attachmentType.equalsIgnoreCase(VIDEO)) fileFolder += VIDEO;
        else if(attachmentType.equalsIgnoreCase(DOCUMENT)) fileFolder += DOCUMENT;

        for (MultipartFile file : files) {
            try {
                File currentFile = new File(gulfNetTMTServiceConfig.getBaseMediaDirectory() + fileFolder ,
                        System.currentTimeMillis() + "_" + attachmentType + "_" + file.getOriginalFilename());

                Attachment item = Attachment.builder()
                        .fileName(file.getOriginalFilename())
                        .fileType(file.getContentType())
                        .fileLocation(URLEncoder.encode(currentFile.getAbsolutePath(),StandardCharsets.UTF_8))
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

    public Resource loadFileAsResource(String fileName) {
        try {

            Path filePath = Paths.get(gulfNetTMTServiceConfig.getBaseMediaDirectory()).resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found: " + fileName);
            }
        } catch (MalformedURLException | FileNotFoundException ex) {
            throw new GulfNetTMTException(ErrorConstants.SYSTEM_ERROR_CODE, ex.getMessage());
        }
    }
}

