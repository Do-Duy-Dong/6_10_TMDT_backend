package com.tmdt.shop_service.modules.categories.infrastructure.controller;

import com.tmdt.shop_service.modules.categories.application.service.CategoryService;
import com.tmdt.shop_service.modules.categories.application.service.ModuleCategoryService;
import com.tmdt.shop_service.modules.categories.domain.repo.CategoryRepository;
import com.tmdt.shop_service.modules.categories.domain.repo.ModuleCategoryRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Attaches controller")
@RequiredArgsConstructor
public class CategoryController {
    final CategoryService categoryService;
    final ModuleCategoryService moduleCategoryService;
}
