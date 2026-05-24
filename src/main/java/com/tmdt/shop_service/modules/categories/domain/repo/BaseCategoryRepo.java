package com.tmdt.shop_service.modules.categories.domain.repo;

import com.tmdt.shop_service.modules.categories.application.dto.BaseCategoryDto;
import com.tmdt.shop_service.modules.categories.domain.model.BaseCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BaseCategoryRepo {
    BaseCategory save(BaseCategory baseCategory);

    Optional<BaseCategory> findById(Long id);

    Optional<BaseCategory> findByCode(String code);

    void delete(Long id);

    Page<BaseCategoryDto> getList(
            Pageable pageable,
            String nameCt,
            String codeEq,
            Integer isActive);
}
