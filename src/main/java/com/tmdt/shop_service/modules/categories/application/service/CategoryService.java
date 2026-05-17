package com.tmdt.shop_service.modules.categories.application.service;

import com.tmdt.shop_service.modules.categories.application.dto.CategoryDto;
import com.tmdt.shop_service.modules.categories.application.request.CreateCategoryRequest;
import com.tmdt.shop_service.modules.categories.application.request.UpdateCategoryRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto create(@NotNull CreateCategoryRequest request, @NotNull Long userId);

    CategoryDto update(
            @NotNull Long id,
            @NotNull UpdateCategoryRequest request,
            @NotNull Long userId);

    void updateStatus(Long id, Integer status);

    void delete(@NotNull Long id);

    CategoryDto getById(Long id);

    Page<CategoryDto> getList(
            Pageable pageable,
            String nameCt,
            String codeEq,
            String baseCodeEq,
            Integer isActive);

    CategoryDto getByIdHasStatusActive(Long id);
}
