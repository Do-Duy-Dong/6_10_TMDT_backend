package com.tmdt.shop_service.modules.categories.application.service;

import com.tmdt.shop_service.modules.categories.application.dto.BaseCategoryDto;
import com.tmdt.shop_service.modules.categories.application.request.CreateBaseCategoryRequest;
import com.tmdt.shop_service.modules.categories.application.request.UpdateBaseCategoryRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaseCategoryService {
    BaseCategoryDto create(@NotNull CreateBaseCategoryRequest request, @NotNull Long userId);

    BaseCategoryDto update(
            @NotNull Long id,
            @NotNull UpdateBaseCategoryRequest request,
            @NotNull Long userId);

    void updateStatus(Long id, Integer status);

    void delete(@NotNull Long id);

    BaseCategoryDto getById(Long id);

    Page<BaseCategoryDto> getList(
            Pageable pageable,
            String nameCt,
            String codeEq,
            Integer isActive);

    BaseCategoryDto getByIdHasStatusActive(Long id);
}
