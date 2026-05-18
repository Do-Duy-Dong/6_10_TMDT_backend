package com.tmdt.shop_service.modules.warehouse.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateWarehouseRequest {
    @NotNull
    @NotBlank(message = "Tên kho không được để trống")
    private String name;

    @NotNull @NotBlank(message = "Địa chỉ không được để trống")
    private String address;

    private Integer isActive = 1;
}
