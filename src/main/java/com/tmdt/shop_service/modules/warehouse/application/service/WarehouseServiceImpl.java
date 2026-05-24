package com.tmdt.shop_service.modules.warehouse.application.service;

import com.tmdt.shop_service.modules.warehouse.application.dto.WarehouseDto;
import com.tmdt.shop_service.modules.warehouse.application.mapper.WarehouseMapper;
import com.tmdt.shop_service.modules.warehouse.application.request.CreateWarehouseRequest;
import com.tmdt.shop_service.modules.warehouse.application.request.UpdateWarehouseRequest;
import com.tmdt.shop_service.modules.warehouse.domain.model.Warehouse;
import com.tmdt.shop_service.modules.warehouse.domain.repo.WarehouseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseServiceImpl implements WarehouseService {
    private final WarehouseRepo warehouseRepo;
    private final WarehouseMapper warehouseMapper;

    @Override
    public WarehouseDto createWarehouse(CreateWarehouseRequest request) {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(request.getName());
        warehouse.setAddress(request.getAddress());
        warehouse.setIsActive(request.getIsActive());
        return warehouseMapper.toDto(warehouseRepo.save(warehouse));
    }

    @Override
    public WarehouseDto updateWarehouse(Long id, UpdateWarehouseRequest request) {
        Warehouse warehouse = warehouseRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        
        if (request.getName() != null) warehouse.setName(request.getName());
        if (request.getAddress() != null) warehouse.setAddress(request.getAddress());
        if (request.getIsActive() != null) warehouse.setIsActive(request.getIsActive());
        
        return warehouseMapper.toDto(warehouseRepo.save(warehouse));
    }

    @Override
    @Transactional(readOnly = true)
    public WarehouseDto getWarehouseById(Long id) {
        return warehouseRepo.findById(id)
                .map(warehouseMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
    }

    @Override
    public Page<WarehouseDto> getAllWarehousesByParams(Pageable pageable, String nameCt, Integer isActive) {
        return warehouseRepo.getAllWarehousesByParams(pageable, nameCt, isActive);
    }

    @Override
    public void deleteWarehouse(Long id) {
        warehouseRepo.deleteById(id);
    }
}
