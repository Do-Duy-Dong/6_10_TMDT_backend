package com.tmdt.shop_service.modules.warehouse.domain.repo;

import com.tmdt.shop_service.modules.warehouse.domain.model.Warehouse;
import java.util.List;
import java.util.Optional;

public interface WarehouseRepo {
    Warehouse save(Warehouse warehouse);
    Optional<Warehouse> findById(Long id);
    List<Warehouse> findAll();
    void deleteById(Long id);
}
