package com.tmdt.shop_service.modules.users.application.dto;

import com.tmdt.shop_service.modules.users.domain.GenderType;
import lombok.Getter;

public record UserDto(
        Long id,
        String fullName,
        GenderType gender,
        String address,
        Integer isActive,
        String email,
        String phoneNumber) {}
