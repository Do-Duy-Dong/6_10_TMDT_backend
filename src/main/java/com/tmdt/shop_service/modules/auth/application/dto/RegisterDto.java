package com.tmdt.shop_service.modules.auth.application.dto;

import lombok.*;
import org.checkerframework.checker.units.qual.A;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterDto {
    private String email;
    private String fullName;
}
