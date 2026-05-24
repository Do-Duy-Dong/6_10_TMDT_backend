package com.tmdt.shop_service.modules.cart.application.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartRequest {
    @NotNull(message = "Option Id không được để trống")
    private Long optionId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    private Integer quantity;
}
