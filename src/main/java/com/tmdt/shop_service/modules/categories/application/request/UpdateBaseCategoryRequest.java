package com.tmdt.shop_service.modules.categories.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateBaseCategoryRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Mã không được để trống")
    private String code;

    @NotNull(message = "Trạng thái không được để trống")
    private Integer isActive;
}
