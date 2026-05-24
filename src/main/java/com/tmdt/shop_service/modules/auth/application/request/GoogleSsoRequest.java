package com.tmdt.shop_service.modules.auth.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GoogleSsoRequest {
    @NotNull @NotBlank
    private String state;

    @NotNull @NotBlank
    private String iss;

    @NotNull @NotBlank
    private String code;

    @NotNull @NotBlank
    private String scope;

    @NotNull
    private Long authuser;

    @NotNull @NotBlank
    private String prompt;
}
