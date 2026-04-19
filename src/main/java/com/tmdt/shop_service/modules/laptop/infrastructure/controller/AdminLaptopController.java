package com.tmdt.shop_service.modules.laptop.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.application.request.CreateLaptopRequest;
import com.tmdt.shop_service.modules.laptop.application.request.UpdateLaptopRequest;
import com.tmdt.shop_service.modules.laptop.application.service.LaptopService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin Laptop Controller")
@RequestMapping("/v1/admin/laptops")
public class AdminLaptopController {
    final LaptopService laptopService;

    @PostMapping
    public ResponseEntity<LaptopDto> createLaptop(
            @RequestBody @Valid CreateLaptopRequest request,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        LaptopDto laptopDto = laptopService.create(request, userDetail.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(laptopDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaptopDto> updateLaptop(
            @PathVariable Long id,
            @RequestBody @Valid UpdateLaptopRequest request,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        LaptopDto laptopDto = laptopService.update(id, request, userDetail.getId());
        return ResponseEntity.ok(laptopDto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Api thay đổi trạng thái của laptop")
    public ResponseEntity updateStatusForLaptop(
            @PathVariable Long id,
            @RequestParam(value = "status") Integer status) {
        laptopService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Api xóa cứng laptop")
    public ResponseEntity deleteLaptop(@PathVariable Long id) {
        laptopService.deleteLaptop(id);
        return ResponseEntity.ok().build();
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
            @RequestParam(value = "isActive", required = false) Integer isActive,
            @RequestParam(value = "originalPrice:ge", required = false) BigDecimal originalPriceGe,
            @RequestParam(value = "originalPrice:le", required = false) BigDecimal originalPriceLe) {

        Page<LaptopDto> page = laptopService.getList(pageable, nameCt, isActive, originalPriceGe, originalPriceLe);
        Integer nextPage = page.hasNext() ? page.getNumber() + 1 : null;
        return new CollectionResponse<>(
                page.getContent(),
                nextPage,
                page.getTotalElements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaptopDto> getLaptopById(@PathVariable Long id) {
        var result = laptopService.getById(id);
        return ResponseEntity.ok().body(result);
    }
}
