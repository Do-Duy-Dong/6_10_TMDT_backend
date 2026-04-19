package com.tmdt.shop_service.modules.categories.application.dto;


import java.time.LocalDateTime;

public record CategoryDto(
        Long id,
        String name,
        String code,
        Integer is_active,
        LocalDateTime updateAt,
        LocalDateTime createAt) {}
