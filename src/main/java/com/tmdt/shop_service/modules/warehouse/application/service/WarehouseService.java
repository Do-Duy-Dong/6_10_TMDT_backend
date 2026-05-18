package com.tmdt.shop_service.modules.warehouse.application.service;

import com.tmdt.shop_service.modules.warehouse.application.dto.WarehouseDto;
import com.tmdt.shop_service.modules.warehouse.application.request.CreateWarehouseRequest;
import com.tmdt.shop_service.modules.warehouse.application.request.UpdateWarehouseRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WarehouseService {
    WarehouseDto createWarehouse(CreateWarehouseRequest request);
    WarehouseDto updateWarehouse(Long id, UpdateWarehouseRequest request);
    WarehouseDto getWarehouseById(Long id);
    Page<WarehouseDto> getAllWarehousesByParams(Pageable pageable, String nameCt, Integer isActive);
    void deleteWarehouse(Long id);
}
