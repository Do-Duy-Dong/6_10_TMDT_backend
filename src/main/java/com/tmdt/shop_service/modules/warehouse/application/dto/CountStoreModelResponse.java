package com.tmdt.shop_service.modules.warehouse.application.dto;

import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountStoreModelResponse {
    String laptopName;
    String warehouseName;
    Long warehouseId;
    Long laptopId;
    StoreModelStatus status;
    Long quantity;
}
