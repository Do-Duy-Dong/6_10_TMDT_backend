package com.tmdt.shop_service.modules.laptop.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.application.service.LaptopService;
import com.tmdt.shop_service.utils.Constant;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/public/laptops")
@Tag(name = "Public api for laptop")
public class LaptopController {
    final LaptopService laptopService;

    @GetMapping("/{id}")
    public ResponseEntity<LaptopDto> getLaptopById(@PathVariable Long id) {
        var result = laptopService.getLaptopByIdHasStatusActive(id);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    public CollectionResponse<LaptopDto> getList(
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "create_at",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "name:ct", required = false) String nameCt,
            @RequestParam(value = "originalPrice:ge", required = false) BigDecimal originalPriceGe,
            @RequestParam(value = "originalPrice:le", required = false) BigDecimal originalPriceLe) {

        // For public, we always force isActive = Constant.STATUS.ACTIVE
        Page<LaptopDto> page = laptopService.getList(pageable, nameCt, Constant.STATUS.ACTIVE, originalPriceGe, originalPriceLe);
        Integer nextPage = page.hasNext() ? page.getNumber() + 1 : null;
        return new CollectionResponse<>(
                page.getContent(),
                nextPage,
                page.getTotalElements());
    }
}
