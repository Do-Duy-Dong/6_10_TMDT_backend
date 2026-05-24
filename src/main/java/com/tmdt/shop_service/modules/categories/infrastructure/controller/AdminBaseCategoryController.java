package com.tmdt.shop_service.modules.categories.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import com.tmdt.shop_service.modules.categories.application.dto.BaseCategoryDto;
import com.tmdt.shop_service.modules.categories.application.request.CreateBaseCategoryRequest;
import com.tmdt.shop_service.modules.categories.application.request.UpdateBaseCategoryRequest;
import com.tmdt.shop_service.modules.categories.application.service.BaseCategoryService;
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

@RestController
@RequiredArgsConstructor
@Tag(name = "Admin Base Category Controller")
@RequestMapping("/v1/admin/base-categories")
public class AdminBaseCategoryController {
    final BaseCategoryService baseCategoryService;

    @PostMapping
    public ResponseEntity<BaseCategoryDto> create(
            @RequestBody @Valid CreateBaseCategoryRequest request,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        BaseCategoryDto dto = baseCategoryService.create(request, userDetail.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseCategoryDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateBaseCategoryRequest request,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        BaseCategoryDto dto = baseCategoryService.update(id, request, userDetail.getId());
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Api thay đổi trạng thái của category")
    public ResponseEntity updateStatus(
            @PathVariable Long id,
            @RequestParam(value = "status") Integer status) {
        baseCategoryService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Api xóa cứng category")
    public ResponseEntity delete(@PathVariable Long id) {
        baseCategoryService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public CollectionResponse<BaseCategoryDto> getList(
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "create_at",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "name:ct", required = false) String nameCt,
            @RequestParam(value = "code:eq", required = false) String codeEq,
            @RequestParam(value = "isActive", required = false) Integer isActive) {

        Page<BaseCategoryDto> page = baseCategoryService.getList(pageable, nameCt, codeEq, isActive);
        Integer nextPage = page.hasNext() ? page.getNumber() + 1 : null;
        return new CollectionResponse<>(
                page.getContent(),
                nextPage,
                page.getTotalElements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseCategoryDto> getById(@PathVariable Long id) {
        var result = baseCategoryService.getById(id);
        return ResponseEntity.ok().body(result);
    }
}
