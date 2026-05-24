package com.tmdt.shop_service.modules.categories.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.categories.application.dto.CategoryDto;
import com.tmdt.shop_service.modules.categories.application.service.CategoryService;
import com.tmdt.shop_service.modules.categories.application.service.ModuleCategoryService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/public/categories")
@Tag(name = "Public api for category")
public class CategoryController {
    final CategoryService categoryService;
    final ModuleCategoryService moduleCategoryService;

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long id) {
        var result = categoryService.getByIdHasStatusActive(id);
        return ResponseEntity.ok().body(result);
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
            @RequestParam(value = "baseCode:eq", required = false) String baseCodeEq) {

        Page<CategoryDto> page = categoryService.getList(pageable, nameCt, codeEq, baseCodeEq, Constant.STATUS.ACTIVE);
        Integer nextPage = page.hasNext() ? page.getNumber() + 1 : null;
        return new CollectionResponse<>(
                page.getContent(),
                nextPage,
                page.getTotalElements());
    }
}
