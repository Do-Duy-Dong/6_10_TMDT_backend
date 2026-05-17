package com.tmdt.shop_service.modules.categories.application.service;

import com.tmdt.shop_service.core.exception.ResourceNotFoundException;
import com.tmdt.shop_service.modules.categories.application.dto.BaseCategoryDto;
import com.tmdt.shop_service.modules.categories.application.mapper.BaseCategoryMapper;
import com.tmdt.shop_service.modules.categories.application.request.CreateBaseCategoryRequest;
import com.tmdt.shop_service.modules.categories.application.request.UpdateBaseCategoryRequest;
import com.tmdt.shop_service.modules.categories.domain.model.BaseCategory;
import com.tmdt.shop_service.modules.categories.domain.repo.BaseCategoryRepo;
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
public class BaseCategoryServiceImpl implements BaseCategoryService {
    final BaseCategoryRepo baseCategoryRepo;

    @Override
    public BaseCategoryDto create(@NotNull CreateBaseCategoryRequest request, @NotNull Long userId) {
        BaseCategory baseCategory = new BaseCategory(
                request.getName(),
                request.getCode().trim().toUpperCase(),
                request.getIsActive()
        );

        baseCategory = baseCategoryRepo.save(baseCategory);

        return BaseCategoryMapper.INSTANCE.toDto(baseCategory);
    }

    @Override
    public BaseCategoryDto update(
            @NotNull Long id,
            @NotNull UpdateBaseCategoryRequest request,
            @NotNull Long userId) {
        BaseCategory baseCategory = baseCategoryRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BaseCategory Not Found"));
        baseCategory.setName(request.getName());
        baseCategory.setCode(request.getCode());
        baseCategory.setIsActive(request.getIsActive());
        baseCategory = baseCategoryRepo.save(baseCategory);
        return BaseCategoryMapper.INSTANCE.toDto(baseCategory);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        BaseCategory baseCategory = baseCategoryRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BaseCategory Not Found"));
        baseCategory.setIsActive(status);
        baseCategoryRepo.save(baseCategory);
    }

    @Override
    public void delete(@NotNull Long id) {
        BaseCategory baseCategory = baseCategoryRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BaseCategory Not Found"));
        baseCategoryRepo.delete(id);
    }

    @Override
    public BaseCategoryDto getById(Long id) {
        BaseCategory baseCategory = baseCategoryRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BaseCategory Not Found"));
        return BaseCategoryMapper.INSTANCE.toDto(baseCategory);
    }

    @Override
    public Page<BaseCategoryDto> getList(Pageable pageable, String nameCt, String codeEq, Integer isActive) {
        return baseCategoryRepo.getList(pageable, nameCt, codeEq, isActive);
    }

    @Override
    public BaseCategoryDto getByIdHasStatusActive(Long id) {
        BaseCategory baseCategory = baseCategoryRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("BaseCategory Not Found"));
        if (!Constant.STATUS.ACTIVE.equals(baseCategory.getIsActive())) {
            throw new ResourceNotFoundException("BaseCategory is not active or not found");
        }
        return BaseCategoryMapper.INSTANCE.toDto(baseCategory);
    }
}
