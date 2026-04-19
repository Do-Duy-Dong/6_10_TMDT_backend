package com.tmdt.shop_service.modules.categories.infrastructure.repository;

import com.tmdt.shop_service.modules.categories.domain.repo.CategoryRepository;
import com.tmdt.shop_service.modules.categories.infrastructure.jpa.JpaCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    final JpaCategoryRepository jpaCategoryRepository;
}
