package com.tmdt.shop_service.modules.fileupload.application.service;

import com.tmdt.shop_service.modules.fileupload.application.dto.PresignedUrlDto;
import com.tmdt.shop_service.modules.fileupload.infrastructure.config.S3Config;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService{
    final String contentType = "application/octet-stream";
    final S3Config s3Config;
    final S3Presigner s3Presigner;
    final S3Client s3Client;

    @Override
    public PresignedUrlDto getPresignedUrl(String fileName, long expirationInMinutes, boolean isPublic) {
        String keyName = generateKeyName(fileName, isPublic);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3Config.getBucket())
                .key(keyName)
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .putObjectRequest(putObjectRequest)
                .signatureDuration(Duration.ofMinutes(expirationInMinutes))
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(putObjectPresignRequest);

        String presignedUrl = presignedPutObjectRequest.url().toString();

        return new PresignedUrlDto (
                presignedUrl,
                keyName);
    }

    @Override
    public Object getObjectInfo(String keyName) {
        HeadObjectRequest headObjectRequest = HeadObjectRequest
                .builder()
                .bucket(s3Config.getBucket())
                .key(keyName)
                .build();

        HeadObjectResponse headObjectResponse = s3Client.headObject(headObjectRequest);

        return headObjectResponse;
    }

    String generateKeyName(String fileName, boolean isPublic) {
        String keyName = (isPublic ? "public/" : "private/");
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month =  now.getMonthValue();
        keyName += String.format(
                "%s/%s/%s-%s",
                year,
                month,
                now.atZone(ZoneId.systemDefault()).toEpochSecond(),
                fileName);
        return keyName;
    }
}
