package com.tmdt.shop_service.modules.categories.application.service;

import com.tmdt.shop_service.core.exception.ResourceNotFoundException;
import com.tmdt.shop_service.modules.categories.application.dto.CategoryDto;
import com.tmdt.shop_service.modules.categories.application.mapper.CategoryMapper;
import com.tmdt.shop_service.modules.categories.application.request.CreateCategoryRequest;
import com.tmdt.shop_service.modules.categories.application.request.UpdateCategoryRequest;
import com.tmdt.shop_service.modules.categories.domain.model.BaseCategory;
import com.tmdt.shop_service.modules.categories.domain.model.Category;
import com.tmdt.shop_service.modules.categories.domain.repo.BaseCategoryRepo;
import com.tmdt.shop_service.modules.categories.domain.repo.CategoryRepository;
import com.tmdt.shop_service.utils.Constant;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    final CategoryRepository categoryRepository;
    final BaseCategoryRepo baseCategoryRepo;

    @Override
    public CategoryDto create(@NotNull CreateCategoryRequest request, @NotNull Long userId) {
        BaseCategory baseCategory = baseCategoryRepo.findById(request.getBaseCodeId()).orElseThrow(
                () -> new ResourceNotFoundException("BaseCategory Not Found"));

        Category category = new Category();
        category.setName(request.getName());
        category.setCode(request.getCode());
        category.setBaseCode(baseCategory.getCode());
        category.setIsActive(request.getIsActive());

        category = categoryRepository.save(category);

        CategoryDto dto = CategoryMapper.INSTANCE.toDto(category);
        dto.setBaseCodeId(baseCategory.getId());
        dto.setBaseCode(baseCategory.getCode());
        dto.setBaseCodeName(baseCategory.getName());
        return dto;
    }

    @Override
    public CategoryDto update(
            @NotNull Long id,
            @NotNull UpdateCategoryRequest request,
            @NotNull Long userId) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category Not Found"));

        BaseCategory baseCategory = baseCategoryRepo.findById(request.getBaseCodeId()).orElseThrow(
                () -> new ResourceNotFoundException("BaseCategory Not Found"));

        category.setName(request.getName());
        category.setCode(request.getCode().trim().toUpperCase());
        category.setBaseCode(baseCategory.getCode());
        category.setIsActive(request.getIsActive());

        category = categoryRepository.save(category);

        CategoryDto dto = CategoryMapper.INSTANCE.toDto(category);
        dto.setBaseCodeId(baseCategory.getId());
        dto.setBaseCode(baseCategory.getCode());
        dto.setBaseCodeName(baseCategory.getName());
        return dto;
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category Not Found"));
        category.setIsActive(status);
        categoryRepository.save(category);
    }

    @Override
    public void delete(@NotNull Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category Not Found"));
        categoryRepository.delete(id);
    }

    @Override
    public CategoryDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category Not Found"));
        CategoryDto dto = CategoryMapper.INSTANCE.toDto(category);

        if (category.getBaseCode() != null) {
            baseCategoryRepo.findByCode(category.getBaseCode()).ifPresent(baseCategory -> {
                dto.setBaseCodeId(baseCategory.getId());
                dto.setBaseCode(baseCategory.getCode());
                dto.setBaseCodeName(baseCategory.getName());
            });
        }

        return dto;
    }

    @Override
    public Page<CategoryDto> getList(
            Pageable pageable,
            String nameCt,
            String codeEq,
            String baseCodeEq,
            Integer isActive) {
        return categoryRepository.getList(pageable, nameCt, codeEq, baseCodeEq, isActive);
    }

    @Override
    public CategoryDto getByIdHasStatusActive(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Category Not Found"));
        if (!Constant.STATUS.ACTIVE.equals(category.getIsActive())) {
            throw new ResourceNotFoundException("Category is not active or not found");
        }

        CategoryDto dto = CategoryMapper.INSTANCE.toDto(category);

        if (category.getBaseCode() != null) {
            baseCategoryRepo.findByCode(category.getBaseCode()).ifPresent(baseCategory -> {
                dto.setBaseCodeId(baseCategory.getId());
                dto.setBaseCode(baseCategory.getCode());
                dto.setBaseCodeName(baseCategory.getName());
            });
        }

        return dto;
    }
}
