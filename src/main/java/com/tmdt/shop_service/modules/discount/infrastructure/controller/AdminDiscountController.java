package com.tmdt.shop_service.modules.discount.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import com.tmdt.shop_service.modules.discount.application.dto.DiscountDto;
import com.tmdt.shop_service.modules.discount.application.request.CreateDiscountRequest;
import com.tmdt.shop_service.modules.discount.application.request.UpdateDiscountRequest;
import com.tmdt.shop_service.modules.discount.application.service.DiscountService;
import com.tmdt.shop_service.modules.discount.domain.DiscountType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin Discount Controller")
@RequestMapping("/v1/admin/discounts")
public class AdminDiscountController {
    final DiscountService discountService;

    @GetMapping("/{id}")
    public ResponseEntity<DiscountDto> getById(@PathVariable Long id) {
        var result = discountService.getById(id);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<DiscountDto> create(
            @RequestBody CreateDiscountRequest request,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        DiscountDto discountDto = discountService.create(request, userDetail.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(discountDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscountDto> update(
            @PathVariable Long id,
            @RequestBody UpdateDiscountRequest request,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        DiscountDto discountDto = discountService.update(id, request, userDetail.getId());
        return ResponseEntity.ok(discountDto);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Api delete discount")
    public ResponseEntity delete(@PathVariable Long id) {
        discountService.delete(id);
        return ResponseEntity.ok().build();
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
            @RequestParam(value = "type:eq", required = false) DiscountType typeEq,
            @RequestParam(value = "isActive:eq", required = false)  Integer isActive,
            @RequestParam(value = "expiryFrom:ge", required = false) LocalDateTime expiryFromGe,
            @RequestParam(value = "expiryFrom:le", required = false) LocalDateTime expiryFromLe) {

        Page<DiscountDto> page = discountService.getList(pageable, nameCt, codeEq, typeEq, isActive, expiryFromGe, expiryFromLe, null, null);
        Integer nextPage = page.hasNext() ? page.getNumber() + 1 : null;
        return new CollectionResponse<DiscountDto>(
                page.getContent(),
                nextPage,
                page.getTotalElements());
    }
}
