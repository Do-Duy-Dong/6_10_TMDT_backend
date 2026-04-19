package com.tmdt.shop_service.modules.categories.application.dto;


import com.tmdt.shop_service.modules.categories.domain.ModuleCategoryType;

import java.time.LocalDateTime;

public record ModuleCategoryDto(
        Long id,
        String moduleId,
        String categoryId,
        ModuleCategoryType type,
        Integer isActive) {}
