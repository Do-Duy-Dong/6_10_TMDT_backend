package com.tmdt.shop_service.modules.laptop.application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.aop.target.LazyInitTargetSource;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateLaptopRequest {
    @NotBlank(message = "Tên không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @NotNull(message = "Trạng thái không được để trống")
    private Integer isActive;

    @NotNull(message = "Giá không được để trống")
    private BigDecimal originalPrice;

    private List<Long> attachIds;

    private Long parentId;

    @NotNull
    private Long brandId;

    @NotNull
    private Long ramId;

    @NotNull
    private Long storageId;

    @NotNull
    private Long screenSizeId;

    @NotNull
    private Long gpuId;

    @NotNull
    private Long cpuId;

    @NotNull
    private Long screenId;
}
