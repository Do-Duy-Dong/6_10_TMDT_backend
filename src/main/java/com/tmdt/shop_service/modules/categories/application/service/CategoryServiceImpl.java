package com.tmdt.shop_service.modules.categories.application.service;

import com.tmdt.shop_service.modules.categories.domain.repo.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    final CategoryRepository auditRepository;
}
