package com.tmdt.shop_service.modules.warehouse.application.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateWarehouseRequest {
    @NotBlank(message = "Tên kho không được để trống")
    private String name;
    private String address;
    private Integer isActive = 1;
}
