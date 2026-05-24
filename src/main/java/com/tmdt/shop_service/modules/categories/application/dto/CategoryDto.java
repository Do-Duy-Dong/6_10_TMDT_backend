package com.tmdt.shop_service.modules.categories.application.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String name;
    private String code;
    private Integer isActive;
    private Long baseCodeId;
    private String baseCode;
    private String baseCodeName;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
