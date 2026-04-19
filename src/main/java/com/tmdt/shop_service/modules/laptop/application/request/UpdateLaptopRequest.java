package com.tmdt.shop_service.modules.laptop.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateLaptopRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Trạng thái không được để trống")
    private Integer isActive;

    @NotNull(message = "Giá không được để trống")
    private BigDecimal originalPrice;
}
