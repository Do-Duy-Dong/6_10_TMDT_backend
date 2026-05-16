package com.tmdt.shop_service.modules.fileupload.infrastructure.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {
    final String region;
    final String access;
    final String secret;
    @Getter final String bucket;

    public S3Config(
            @Value("${aws.region}") String region,
            @Value("${aws.access}") String access,
            @Value("${aws.secret}") String secret,
            @Value("${aws.bucket}") String bucket) {
        this.region = region;
        this.access = access;
        this.secret = secret;
        this.bucket = bucket;
    }

    @Bean
    public S3Presigner s3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(access, secret)))
                .build();
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(access, secret)))
                .build();
    }
}
