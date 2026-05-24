package com.tmdt.shop_service.modules.warehouse.domain.repo;

import com.tmdt.shop_service.modules.warehouse.application.dto.CountStoreModelResponse;
import com.tmdt.shop_service.modules.warehouse.application.dto.StoreModelDto;
import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import com.tmdt.shop_service.modules.warehouse.domain.model.StoreModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StoreModelRepo {
    StoreModel save(StoreModel storeModel);
    Optional<StoreModel> findById(Long id);
    List<StoreModel> findAll();
    List<StoreModel> findByWarehouseId(Long warehouseId);
    void deleteById(Long id);
    Optional<StoreModel> findBySerialNumber(String serialNumber);
    List<StoreModel> findBySerialNumbers(List<String> serialNumbers);
    Page<StoreModel> findByWarehouseIdAndStatus(
            Pageable pageable,
            Long warehouseId,
            StoreModelStatus status);

    Page<CountStoreModelResponse> getStoreModelByParams(Pageable pageable, String nameLaptopCt, Long warehouseId, List<StoreModelStatus> statusIn);

    List<StoreModel> saveAll(List<StoreModel> storeModels);
}
