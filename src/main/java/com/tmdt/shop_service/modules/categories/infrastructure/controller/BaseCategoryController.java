package com.tmdt.shop_service.modules.categories.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.categories.application.dto.BaseCategoryDto;
import com.tmdt.shop_service.modules.categories.application.service.BaseCategoryService;
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
@RequestMapping("/v1/public/base-categories")
@Tag(name = "Public api for base category")
public class BaseCategoryController {
    final BaseCategoryService baseCategoryService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseCategoryDto> getById(@PathVariable Long id) {
        var result = baseCategoryService.getByIdHasStatusActive(id);
        return ResponseEntity.ok().body(result);
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
            @RequestParam(value = "code:eq", required = false) String codeEq) {

        // For public, we always force isActive = Constant.STATUS.ACTIVE
        Page<BaseCategoryDto> page = baseCategoryService.getList(pageable, nameCt, codeEq, Constant.STATUS.ACTIVE);
        Integer nextPage = page.hasNext() ? page.getNumber() + 1 : null;
        return new CollectionResponse<>(
                page.getContent(),
                nextPage,
                page.getTotalElements());
    }
}
