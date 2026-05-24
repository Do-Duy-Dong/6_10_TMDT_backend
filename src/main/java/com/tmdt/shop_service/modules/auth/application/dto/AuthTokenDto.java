package com.tmdt.shop_service.modules.auth.application.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthTokenDto {
    String accessToken;
    String refreshToken;
    Long expiry;
}
