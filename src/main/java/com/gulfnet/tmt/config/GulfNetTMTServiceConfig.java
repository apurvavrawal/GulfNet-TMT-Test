package com.gulfnet.tmt.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
public class GulfNetTMTServiceConfig {

    @Value("${aws.accessKeyId}")
    private String accessKeyId;

    @Value("${aws.secretKey}")
    private String accessKeySecret;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.s3.bucket-name}")
    private String s3BucketName;

    @Value("${local.file.storage.path}")
    private String localFilePath;

    @Value("${file.upload.maxSize}")
    private Long maxFileUploadSize;

    @Value("${file.upload.extensions}")
    private List<String> allowedFileExt;

    @Value("${default.user.language}")
    private String defaultUserLanguage;

    @Value("${regex.phone.regexp}")
    private String regExPhone;

    @Value("${regex.email.regexp}")
    private String regExEmail;
}
