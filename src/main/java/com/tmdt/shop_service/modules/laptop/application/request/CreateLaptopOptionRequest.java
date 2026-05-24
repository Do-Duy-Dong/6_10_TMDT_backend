package com.tmdt.shop_service.modules.laptop.application.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateLaptopOptionRequest {
    Long id;

    @NotNull
    String name;

    @NotNull
    BigDecimal price;

    @NotNull
    Long attachId;
}
