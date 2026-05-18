package com.tmdt.shop_service.modules.warehouse.application.dto;

import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreModelDto {
    private Long id;
    private String name;
    private Integer isActive;
    private Long warehouseId;
    private StoreModelStatus status;
    private String serialNumber;
    private Long laptopId;
    private Long quantity;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
