package com.gulfnet.tmt.externalService;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.gulfnet.tmt.config.GulfNetTMTServiceConfig;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;

@Configuration
@RequiredArgsConstructor
public class AWSService {

    private final GulfNetTMTServiceConfig gulfNetTMTServiceConfig;
    private AmazonS3 getAmazonS3Client() {
        final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(gulfNetTMTServiceConfig.getAccessKeyId(), gulfNetTMTServiceConfig.getAccessKeyId());

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .withRegion(gulfNetTMTServiceConfig.getAwsRegion())
                .build();
    }

    public String uploadNewFile(MultipartFile multipartFile) throws IOException {
        AmazonS3 S3Client = getAmazonS3Client();

        ObjectMetadata data = new ObjectMetadata();
        data.setContentType(multipartFile.getContentType());
        data.setContentLength(multipartFile.getSize());

        PutObjectResult objectResult = S3Client.putObject(gulfNetTMTServiceConfig.getS3BucketName(), multipartFile.getOriginalFilename(), multipartFile.getInputStream(), data);

        URL url = S3Client.getUrl(gulfNetTMTServiceConfig.getS3BucketName(), objectResult.getContentMd5());

        return url.toString();
    }

    public boolean isAWSS3Configured() {
        return StringUtils.isNotEmpty(gulfNetTMTServiceConfig.getAccessKeyId()) &&
                StringUtils.isNotEmpty(gulfNetTMTServiceConfig.getAccessKeySecret()) &&
                StringUtils.isNotEmpty(gulfNetTMTServiceConfig.getAwsRegion()) &&
                StringUtils.isNotEmpty(gulfNetTMTServiceConfig.getS3BucketName());
    }
}
