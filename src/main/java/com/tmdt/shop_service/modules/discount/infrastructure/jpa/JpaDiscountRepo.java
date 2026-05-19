package com.tmdt.shop_service.modules.discount.infrastructure.jpa;

import com.tmdt.shop_service.modules.discount.domain.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaDiscountRepo extends JpaRepository<Discount, Long> {
}
