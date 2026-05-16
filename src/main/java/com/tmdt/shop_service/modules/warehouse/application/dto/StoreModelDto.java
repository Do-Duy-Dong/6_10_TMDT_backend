package com.tmdt.shop_service.modules.warehouse.application.dto;

import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreModelDto {
    private Long id;
    private Long warehouseId;
    private String serialNumber;
    private Long laptopId;
    private StoreModelStatus status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
