package com.tmdt.shop_service.modules.categories.application.service;

import com.tmdt.shop_service.modules.categories.domain.repo.ModuleCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModuleCategoryServiceImpl implements ModuleCategoryService {
    final ModuleCategoryRepository auditRepository;
}
