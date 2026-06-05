package com.tmdt.shop_service.modules.users.application.dto;

import com.tmdt.shop_service.modules.users.domain.GenderType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    Long id;
    String fullName;
    GenderType gender;
    String address;
    Integer isActive;
    String email;
    String phoneNumber;
    List<String> roles;
}
