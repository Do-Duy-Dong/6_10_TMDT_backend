package com.tmdt.shop_service.modules.auth.application.dto;

import com.tmdt.shop_service.modules.users.domain.GenderType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpDto {
    @NotNull
    private String fullName;
    private GenderType gender;
    @NotNull
    private String address;
    @NotNull
    private String email;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String password;
}
