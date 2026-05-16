package com.tmdt.shop_service.modules.warehouse.infrastructure.jpa;

import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import com.tmdt.shop_service.modules.warehouse.domain.model.StoreModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaStoreModelRepo extends JpaRepository<StoreModel, Long> {
    List<StoreModel> findByWarehouseId(Long warehouseId);
    Optional<StoreModel> findBySerialNumber(String serialNumber);
    Page<StoreModel> findByWarehouseIdAndStatus(Pageable pageable, Long warehouseId, StoreModelStatus status);
}
