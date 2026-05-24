package com.tmdt.shop_service.modules.auth.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {
    @NotNull @NotBlank
    private String email;

    @NotNull @NotBlank
    private String password;
}
