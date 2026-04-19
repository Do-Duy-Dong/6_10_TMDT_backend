package com.tmdt.shop_service.modules.attaches.application.dto;

import com.tmdt.shop_service.modules.attaches.domain.AttachType;
import com.tmdt.shop_service.modules.attaches.domain.model.AttachMetadata;
import com.tmdt.shop_service.modules.attaches.domain.model.AttachStorageMetadata;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

public record AttachDto(
        Long id,
        String name,
        String description,
        Integer is_active,
        AttachType type,
        Long moduleId,
        Long ownerId,
        AttachMetadata attachMetadata,
        AttachStorageMetadata attachStorageMetadata,
        LocalDateTime updateAt,
        LocalDateTime createAt) {}
