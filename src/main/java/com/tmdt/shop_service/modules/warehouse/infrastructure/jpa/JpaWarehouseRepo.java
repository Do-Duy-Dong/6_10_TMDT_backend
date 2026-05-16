package com.tmdt.shop_service.modules.warehouse.infrastructure.jpa;

import com.tmdt.shop_service.modules.warehouse.domain.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaWarehouseRepo extends JpaRepository<Warehouse, Long> {
}
