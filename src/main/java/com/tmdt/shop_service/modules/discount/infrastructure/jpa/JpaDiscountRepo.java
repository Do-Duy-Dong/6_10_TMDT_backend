package com.tmdt.shop_service.modules.discount.infrastructure.jpa;

import com.tmdt.shop_service.modules.discount.domain.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JpaDiscountRepo extends JpaRepository<Discount, Long> {

    @Modifying
    @Transactional
    @Query(value = "update discount set quantity = quantity - 1 where quantity >= 1 and id = :discountId", nativeQuery = true)
    int minusDiscount(@Param("discountId") Long discountId);
}
