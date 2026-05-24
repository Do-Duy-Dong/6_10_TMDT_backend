package com.tmdt.shop_service.modules.discount.application.service;

import com.tmdt.shop_service.modules.discount.application.dto.DiscountDto;
import com.tmdt.shop_service.modules.discount.application.request.CreateDiscountRequest;
import com.tmdt.shop_service.modules.discount.application.request.UpdateDiscountRequest;
import com.tmdt.shop_service.modules.discount.domain.DiscountType;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface DiscountService {
    DiscountDto create(@NotNull CreateDiscountRequest request, @NotNull Long userId);

    DiscountDto update(
            @NotNull Long id,
            @NotNull UpdateDiscountRequest request,
            @NotNull Long userId);

    void delete(@NotNull Long id);

    DiscountDto getById(Long id);

    Page<DiscountDto> getList(
            Pageable pageable,
            String nameCt,
            String codeEq,
            DiscountType typeEq,
            Integer isActive,
            LocalDateTime expiryAtGe,
            LocalDateTime expiryAtLe,
            Long userId,
            Long laptopId);
}
