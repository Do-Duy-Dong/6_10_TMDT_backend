package com.tmdt.shop_service.modules.categories.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import com.tmdt.shop_service.modules.categories.application.dto.CategoryDto;
import com.tmdt.shop_service.modules.categories.application.request.CreateCategoryRequest;
import com.tmdt.shop_service.modules.categories.application.request.UpdateCategoryRequest;
import com.tmdt.shop_service.modules.categories.application.service.CategoryService;
import com.tmdt.shop_service.modules.categories.application.service.ModuleCategoryService;
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
@Tag(name = "Admin Categories controller")
@RequiredArgsConstructor
@RequestMapping("/v1/admin/categories")
public class AdminCategoriesController {
    final CategoryService categoryService;
    final ModuleCategoryService moduleCategoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> create(
            @RequestBody @Valid CreateCategoryRequest request,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        CategoryDto dto = categoryService.create(request, userDetail.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateCategoryRequest request,
            @AuthenticationPrincipal CustomUserDetail userDetail) {
        CategoryDto dto = categoryService.update(id, request, userDetail.getId());
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Api thay đổi trạng thái của category")
    public ResponseEntity updateStatus(
            @PathVariable Long id,
            @RequestParam(value = "status") Integer status) {
        categoryService.updateStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Api xóa cứng category")
    public ResponseEntity delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public CollectionResponse<CategoryDto> getList(
            @ParameterObject
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "create_at",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "name:ct", required = false) String nameCt,
            @RequestParam(value = "code:eq", required = false) String codeEq,
            @RequestParam(value = "baseCode:eq", required = false) String baseCodeEq,
            @RequestParam(value = "isActive", required = false) Integer isActive) {

        Page<CategoryDto> page = categoryService.getList(pageable, nameCt, codeEq, baseCodeEq, isActive);
        Integer nextPage = page.hasNext() ? page.getNumber() + 1 : null;
        return new CollectionResponse<>(
                page.getContent(),
                nextPage,
                page.getTotalElements());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long id) {
        var result = categoryService.getById(id);
        return ResponseEntity.ok().body(result);
    }
}
