package com.tmdt.shop_service.modules.warehouse.application.service;

import com.tmdt.shop_service.modules.warehouse.application.dto.WarehouseDto;
import com.tmdt.shop_service.modules.warehouse.application.request.CreateWarehouseRequest;
import com.tmdt.shop_service.modules.warehouse.application.request.UpdateWarehouseRequest;
import java.util.List;

public interface WarehouseService {
    WarehouseDto createWarehouse(CreateWarehouseRequest request);
    WarehouseDto updateWarehouse(Long id, UpdateWarehouseRequest request);
    WarehouseDto getWarehouseById(Long id);
    List<WarehouseDto> getAllWarehouses();
    void deleteWarehouse(Long id);
}
