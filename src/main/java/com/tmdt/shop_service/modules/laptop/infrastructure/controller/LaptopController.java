package com.tmdt.shop_service.modules.laptop.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.application.dto.OptionLaptopDto;
import com.tmdt.shop_service.modules.laptop.application.dto.PublicLaptopDto;
import com.tmdt.shop_service.modules.laptop.application.service.LaptopService;
import com.tmdt.shop_service.modules.warehouse.application.dto.StoreModelDto;
import com.tmdt.shop_service.modules.warehouse.application.dto.WarehouseDto;
import com.tmdt.shop_service.utils.Constant;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/public/laptops")
@Tag(name = "Public api for laptop")
public class LaptopController {
    final LaptopService laptopService;

    @GetMapping("/{slug}")
    public ResponseEntity<LaptopDto> getLaptopById(@PathVariable String slug) {
        var result = laptopService.getLaptopBySlug(slug);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    public CollectionResponse<PublicLaptopDto> getList(
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "create_at",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @RequestParam(value = "name:ct", required = false) String nameCt,
            @RequestParam(value = "brandId", required = false) Long brandId,
            @RequestParam(value = "cpuId", required = false) Long cpuId,
            @RequestParam(value = "ramId", required = false) Long ramId,
            @RequestParam(value = "storageId", required = false) Long storageId,
            @RequestParam(value = "price:ge", required = false) Long priceGe,
            @RequestParam(value = "price:le", required = false) Long priceLe) {

        // For public, we always force isActive = Constant.STATUS.ACTIVE
        Page<PublicLaptopDto> page = laptopService.getListPublicLaptopDtoByParams(
                pageable, nameCt, brandId, cpuId, ramId, storageId, priceGe, priceLe, userDetail.getId());
        Integer nextPage = page.hasNext() ? page.getNumber() + 1 : null;
        return new CollectionResponse<>(
                page.getContent(),
                nextPage,
                page.getTotalElements());
    }

    @GetMapping("/{id}/options/{optionId}/warehouses")
    public CollectionResponse<WarehouseDto> getStoreModelsHasProduct(
            @PathVariable Long id,
            @PathVariable Long optionId) {
        var result = laptopService.getStoreModelHasProduct(id, optionId);
        return new CollectionResponse<>(result, null, (long) result.size());
    }
}
