package com.tmdt.shop_service.modules.warehouse.application.request;

import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateStoreModelRequest {
    @NotNull(message = "Warehouse ID không được để trống")
    private Long warehouseId;

    @NotBlank(message = "Serial number không được để trống")
    private String serialNumber;

    @NotNull(message = "Laptop ID không được để trống")
    private Long laptopId;

    private StoreModelStatus status = StoreModelStatus.NEW;
}
