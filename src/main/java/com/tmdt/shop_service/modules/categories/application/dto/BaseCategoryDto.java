package com.tmdt.shop_service.modules.categories.application.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseCategoryDto {
    private Long id;
    private String name;
    private String code;
    private Integer isActive;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
