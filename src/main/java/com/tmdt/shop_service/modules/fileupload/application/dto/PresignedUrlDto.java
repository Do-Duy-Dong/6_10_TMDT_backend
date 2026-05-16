package com.tmdt.shop_service.modules.fileupload.application.dto;

public record PresignedUrlDto (
        String presignedUrl,
        String keyName) {}
