package com.tmdt.shop_service.modules.warehouse.domain.repo;

import com.tmdt.shop_service.modules.warehouse.application.dto.WarehouseDto;
import com.tmdt.shop_service.modules.warehouse.domain.model.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepo {
    Warehouse save(Warehouse warehouse);
    Optional<Warehouse> findById(Long id);
    List<Warehouse> findAll();
    void deleteById(Long id);

    Page<WarehouseDto> getAllWarehousesByParams(Pageable pageable, String nameCt, Integer isActive);
}
