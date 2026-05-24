package com.tmdt.shop_service.modules.discount.domain.repo;

import com.tmdt.shop_service.modules.discount.application.dto.DiscountDto;
import com.tmdt.shop_service.modules.discount.domain.DiscountType;
import com.tmdt.shop_service.modules.discount.domain.model.Discount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface DiscountRepo {
    Discount save(Discount discount);

    Optional<Discount> findById(Long id);

    void delete(Long id);

    Page<DiscountDto> getList(
            Pageable pageable,
            String nameCt,
            String codeEq,
            DiscountType typeEq,
            Integer isActive,
            LocalDateTime expiryAtGe,
            LocalDateTime expiryAtLe);
}
