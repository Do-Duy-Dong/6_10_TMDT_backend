package com.tmdt.shop_service.modules.warehouse.infrastructure.repo;

import com.tmdt.shop_service.modules.warehouse.domain.model.Warehouse;
import com.tmdt.shop_service.modules.warehouse.domain.repo.WarehouseRepo;
import com.tmdt.shop_service.modules.warehouse.infrastructure.jpa.JpaWarehouseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WarehouseRepoImpl implements WarehouseRepo {
    private final JpaWarehouseRepo jpaWarehouseRepo;

    @Override
    public Warehouse save(Warehouse warehouse) {
        return jpaWarehouseRepo.save(warehouse);
    }

    @Override
    public Optional<Warehouse> findById(Long id) {
        return jpaWarehouseRepo.findById(id);
    }

    @Override
    public List<Warehouse> findAll() {
        return jpaWarehouseRepo.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpaWarehouseRepo.deleteById(id);
    }
}
