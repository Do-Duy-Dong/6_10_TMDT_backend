package com.tmdt.shop_service.modules.categories.infrastructure.jpa;

import com.tmdt.shop_service.modules.categories.domain.model.BaseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaBaseCategoryRepo extends JpaRepository<BaseCategory, Long> {
    Optional<BaseCategory> findByCode(String code);
}
