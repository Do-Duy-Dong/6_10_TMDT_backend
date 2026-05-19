package com.tmdt.shop_service.modules.discount.application.request;

import com.tmdt.shop_service.modules.discount.domain.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreateDiscountRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String code;

    private List<Long> userIds;

    @NotNull
    private Integer quantity;

    private List<Long> moduleIds;

    @NotNull
    private Long value;

    @NotNull
    private DiscountType type;

    private LocalDateTime expiryFrom;

    private LocalDateTime expiryTo;

    @NotNull
    private Integer isActive;
}
