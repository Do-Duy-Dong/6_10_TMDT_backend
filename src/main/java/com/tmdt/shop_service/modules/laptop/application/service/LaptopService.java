package com.tmdt.shop_service.modules.laptop.application.service;

import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.application.dto.OptionLaptopDto;
import com.tmdt.shop_service.modules.laptop.application.dto.PublicLaptopDto;
import com.tmdt.shop_service.modules.laptop.application.request.CreateLaptopRequest;
import com.tmdt.shop_service.modules.laptop.application.request.UpdateLaptopRequest;
import com.tmdt.shop_service.modules.warehouse.application.dto.StoreModelDto;
import com.tmdt.shop_service.modules.warehouse.application.dto.WarehouseDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface LaptopService {
    LaptopDto create(@NotNull CreateLaptopRequest request, @NotNull Long userId);

    LaptopDto update(
            @NotNull Long laptopId,
            @NotNull UpdateLaptopRequest request,
            @NotNull Long userId);

    void updateStatus(Long laptopId, Integer status);

    void deleteLaptop(@NotNull Long id);

    LaptopDto getById(Long id);

    Page<LaptopDto> getList(
            Pageable pageable,
            String nameCt,
            Integer isActive,
            BigDecimal originalPriceGe,
            BigDecimal originalPriceLe);

    LaptopDto getLaptopByIdHasStatusActive(Long id);

    List<LaptopDto> getLaptopByIds(List<Long> ids);

    Page<PublicLaptopDto> getListPublicLaptopDtoByParams(
            Pageable pageable,
            String nameCt,
            Long brandId,
            Long cpuId,
            Long ramId,
            Long storageId,
            Long priceGe,
            Long priceLe,
            Long userId);

    LaptopDto getLaptopBySlug(String slug);

    List<LaptopDto> findByParentIdInOrIdIn(List<Long> parentIdIn);

    List<OptionLaptopDto> getOptionsOfLaptop(Long id);

    List<WarehouseDto> getStoreModelHasProduct(Long laptopId, Long optionId);
}
