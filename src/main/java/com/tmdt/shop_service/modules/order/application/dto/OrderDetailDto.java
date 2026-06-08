package com.tmdt.shop_service.modules.order.application.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderDetailDto {
    private Long id;
    private Long orderId;
    private String name;
    private String laptopName;
    private String laptopSlug;
    private String imageKey;
    private Long optionId;
    private Integer quantity;
    private BigDecimal price;
    private List<Long> storeModelIds;
    private List<String> serialNumbers;
}
