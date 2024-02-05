package com.gulfnet.tmt.service;

import com.gulfnet.tmt.config.GulfNetTMTServiceConfig;
import com.gulfnet.tmt.externalService.AWSService;
import com.gulfnet.tmt.validator.FileUploadValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageService {

    private final AWSService awsService;
    private final FileUploadValidator fileUploadValidator;
    private final GulfNetTMTServiceConfig gulfNetTMTServiceConfig;

    public String uploadFile(MultipartFile file, String type,String path) throws IOException {
        log.info(" trying to upload file {} with size {}", file.getOriginalFilename(), file.getSize());
        fileUploadValidator.validate(file);
        String fileURL = awsService.isAWSS3Configured() ? awsService.uploadNewFile(file) : uploadFileToLocal(file, type, path);
        log.info(" uploaded file path {}", fileURL);
        return fileURL;
    }

    private String uploadFileToLocal(MultipartFile file, String type, String path) throws IOException {
        File localFile = new File(gulfNetTMTServiceConfig.getBaseMediaDirectory()+path, System.currentTimeMillis() + "_" + type + "_" + file.getOriginalFilename());
        file.transferTo(localFile);
        return localFile.getAbsolutePath();
    }
}
