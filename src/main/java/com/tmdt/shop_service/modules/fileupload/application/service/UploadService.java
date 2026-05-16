package com.tmdt.shop_service.modules.fileupload.application.service;

import com.tmdt.shop_service.modules.fileupload.application.dto.PresignedUrlDto;

public interface UploadService {
    PresignedUrlDto getPresignedUrl(String keyName, long expirationInMinutes, boolean isPublic);

    Object getObjectInfo(String keyName);
}
