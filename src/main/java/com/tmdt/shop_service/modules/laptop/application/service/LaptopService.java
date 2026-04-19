package com.tmdt.shop_service.modules.laptop.application.service;

import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.application.request.CreateLaptopRequest;
import com.tmdt.shop_service.modules.laptop.application.request.UpdateLaptopRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface LaptopService {
    LaptopDto create(@NotNull CreateLaptopRequest request, @NotNull Long userId);

    LaptopDto update(
            @NotNull Long laptopId,
            @NotNull UpdateLaptopRequest request,
            @NotNull Long userId);

    void updateStatus(Long laptopId, Integer status);

    void deleteLaptop(@NotNull Long id);

    LaptopDto getById(Long id);

    Page<LaptopDto> getList(
            Pageable pageable,
            String nameCt,
            Integer isActive,
            BigDecimal originalPriceGe,
            BigDecimal originalPriceLe);

    LaptopDto getLaptopByIdHasStatusActive(Long id);
}
