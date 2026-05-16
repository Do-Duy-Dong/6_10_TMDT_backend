package com.tmdt.shop_service.modules.warehouse.infrastructure.repo;

import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import com.tmdt.shop_service.modules.warehouse.domain.model.StoreModel;
import com.tmdt.shop_service.modules.warehouse.domain.repo.StoreModelRepo;
import com.tmdt.shop_service.modules.warehouse.infrastructure.jpa.JpaStoreModelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StoreModelRepoImpl implements StoreModelRepo {
    private final JpaStoreModelRepo jpaStoreModelRepo;

    @Override
    public StoreModel save(StoreModel storeModel) {
        return jpaStoreModelRepo.save(storeModel);
    }

    @Override
    public Optional<StoreModel> findById(Long id) {
        return jpaStoreModelRepo.findById(id);
    }

    @Override
    public List<StoreModel> findAll() {
        return jpaStoreModelRepo.findAll();
    }

    @Override
    public List<StoreModel> findByWarehouseId(Long warehouseId) {
        return jpaStoreModelRepo.findByWarehouseId(warehouseId);
    }

    @Override
    public void deleteById(Long id) {
        jpaStoreModelRepo.deleteById(id);
    }

    @Override
    public Optional<StoreModel> findBySerialNumber(String serialNumber) {
        return jpaStoreModelRepo.findBySerialNumber(serialNumber);
    }

    @Override
    public Page<StoreModel> findByWarehouseIdAndStatus(Pageable pageable, Long warehouseId, StoreModelStatus status) {
        return jpaStoreModelRepo.findByWarehouseIdAndStatus(pageable, warehouseId, status);
    }
}
