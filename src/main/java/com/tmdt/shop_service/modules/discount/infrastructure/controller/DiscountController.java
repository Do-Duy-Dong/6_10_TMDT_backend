package com.tmdt.shop_service.modules.discount.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.discount.application.dto.DiscountDto;
import com.tmdt.shop_service.modules.discount.application.service.DiscountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/public/discounts")
@Tag(name = "Public api for discounts")
public class DiscountController {
    final DiscountService discountService;

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDto> getById(@PathVariable Long id) {
        var result = discountService.getById(id);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping
    public CollectionResponse<DiscountDto> getList(
            @ParameterObject
                    @PageableDefault(
                            page = 0,
                            size = 10,
                            sort = "create_at",
                            direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "name:ct", required = false) String nameCt,
            @RequestParam(value = "code:eq", required = false) String codeEq,
            @RequestParam(value = "expiryAt:ge", required = false) LocalDateTime expiryAtGe,
            @RequestParam(value = "expiryAt:le", required = false) LocalDateTime expiryAtLe) {

        Page<DiscountDto> page = discountService.getList(pageable, nameCt, codeEq, null, 1, expiryAtGe, expiryAtLe);
        Integer nextPage = page.hasNext() ? page.getNumber() + 1 : null;
        return new CollectionResponse<DiscountDto>(
                page.getContent(),
                nextPage,
                page.getTotalElements());
    }
}
