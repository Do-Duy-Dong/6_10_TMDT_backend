package com.tmdt.shop_service.modules.laptop.application.dto;

import com.tmdt.shop_service.modules.attaches.application.dto.AttachDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private Long parentId;
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
