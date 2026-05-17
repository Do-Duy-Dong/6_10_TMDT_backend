package com.tmdt.shop_service.modules.categories.domain.repo;

import com.tmdt.shop_service.modules.categories.application.dto.CategoryDto;
import com.tmdt.shop_service.modules.categories.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CategoryRepository {
    Category save(Category category);

    Optional<Category> findById(Long id);

    void delete(Long id);

    Page<CategoryDto> getList(
            Pageable pageable,
            String nameCt,
            String codeEq,
            String baseCodeEq,
            Integer isActive);
}
