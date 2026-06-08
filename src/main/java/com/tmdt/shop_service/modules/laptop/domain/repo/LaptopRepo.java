package com.tmdt.shop_service.modules.laptop.domain.repo;

import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.application.dto.PublicLaptopDto;
import com.tmdt.shop_service.modules.laptop.domain.model.Laptop;
import com.tmdt.shop_service.modules.warehouse.application.dto.WarehouseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
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

    List<Laptop> findByIds(List<Long> ids);

    Page<PublicLaptopDto> getPublicLaptopDtoByParams(
            Pageable pageable,
            String nameCt,
            Long brandId,
            Long cpuId,
            Long ramId,
            Long storageId,
            Long priceGe,
            Long priceLe,
            Long userId);

    Optional<Laptop> findBySlug(String slug);

    List<Laptop> findLaptopsByParentIdInOrIdIn(List<Long> parentId, List<Long> IdIn);

    List<WarehouseDto> getStoreModelDtoHasProduct(Long laptopId, Long optionId);
}
