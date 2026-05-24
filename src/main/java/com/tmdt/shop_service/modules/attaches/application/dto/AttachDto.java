package com.tmdt.shop_service.modules.attaches.application.dto;

import com.tmdt.shop_service.modules.attaches.domain.AttachType;
import com.tmdt.shop_service.modules.attaches.domain.model.AttachMetadata;
import com.tmdt.shop_service.modules.attaches.domain.model.AttachStorageMetadata;

import java.time.LocalDateTime;

public record AttachDto(
        Long id,
        String name,
        String description,
        Integer isActive,
        AttachType type,
        Long moduleId,
        Long ownerId,
        AttachMetadata attachMetadata,
        AttachStorageMetadata attachStorageMetadata,
        LocalDateTime updateAt,
        LocalDateTime createAt) {}
