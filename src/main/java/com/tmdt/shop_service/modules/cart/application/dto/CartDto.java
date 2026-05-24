package com.tmdt.shop_service.modules.cart.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private Long id;
    private Long laptopId;
    private String laptopName;
    private String laptopSlug;
    private Long optionId;
    private String optionName;
    private Long price;
    private Long originalPrice;
    private String imageKey;
    private String brandName;
    private Long total;
    private Long ownerId;
    private Integer quantity;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
