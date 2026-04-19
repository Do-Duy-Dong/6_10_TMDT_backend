package com.tmdt.shop_service.modules.laptop.domain.repo;

import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.domain.model.Laptop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface LaptopRepo {
    Laptop save(Laptop laptop);

    Optional<Laptop> findById(Long laptopId);

    void delete(Long id);

    Page<LaptopDto> getList(
            Pageable pageable,
            String nameCt,
            Integer isActive,
            BigDecimal originalPriceGe,
            BigDecimal originalPriceLe);
}
