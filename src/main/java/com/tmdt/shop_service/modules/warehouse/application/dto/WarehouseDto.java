package com.tmdt.shop_service.modules.warehouse.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseDto {
    private Long id;
    private String name;
    private String address;
    private Integer isActive;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Long total;
}
