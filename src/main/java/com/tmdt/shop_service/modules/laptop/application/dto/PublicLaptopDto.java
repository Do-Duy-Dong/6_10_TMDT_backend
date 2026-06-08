package com.tmdt.shop_service.modules.laptop.application.dto;

import com.tmdt.shop_service.modules.attaches.application.dto.AttachDto;
import com.tmdt.shop_service.modules.discount.domain.DiscountType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicLaptopDto {
    private Long id;
    private String name;
    private String originalPrice;
    private String price;
    private DiscountType discountType;
    private String discountValue;
    private Long brandId;
    private Long ramId;
    private Long storageId;
    private Long screenSizeId;
    private Long gpuId;
    private Long cpuId;
    private Long screenId;
    private String slug;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<AttachDto> attaches;
}
