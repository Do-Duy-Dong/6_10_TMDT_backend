package com.tmdt.shop_service.modules.warehouse.application.request;

import lombok.Data;

@Data
public class UpdateWarehouseRequest {
    private String name;
    private String address;
    private Integer isActive;
}
