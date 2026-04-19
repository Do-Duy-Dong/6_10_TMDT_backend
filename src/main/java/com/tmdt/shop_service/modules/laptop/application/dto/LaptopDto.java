package com.tmdt.shop_service.modules.laptop.application.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LaptopDto {
    private Long id;
    private String name;
    private String description;
    private Integer isActive;
    private Long createBy;
    private BigDecimal originalPrice;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
