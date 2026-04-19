package com.tmdt.shop_service.modules.categories.infrastructure.jpa;

import com.tmdt.shop_service.modules.categories.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaCategoryRepository extends JpaRepository<Category, Long> {
}
