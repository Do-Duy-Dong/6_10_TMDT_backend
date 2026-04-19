package com.tmdt.shop_service.modules.categories.infrastructure.repository;

import com.tmdt.shop_service.modules.categories.domain.repo.CategoryRepository;
import com.tmdt.shop_service.modules.categories.domain.repo.ModuleCategoryRepository;
import com.tmdt.shop_service.modules.categories.infrastructure.jpa.JpaCategoryRepository;
import com.tmdt.shop_service.modules.categories.infrastructure.jpa.JpaModuleCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ModuleCategoryRepositoryImpl implements ModuleCategoryRepository {
    final JpaModuleCategoryRepository jpaModuleCategoryRepository;
}
