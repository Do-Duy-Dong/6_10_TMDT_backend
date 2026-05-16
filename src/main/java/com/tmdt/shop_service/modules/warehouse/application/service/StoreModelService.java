package com.tmdt.shop_service.modules.warehouse.application.service;

import com.tmdt.shop_service.modules.post.domain.PostStatus;
import com.tmdt.shop_service.modules.warehouse.application.dto.StoreModelDto;
import com.tmdt.shop_service.modules.warehouse.application.request.CreateStoreModelRequest;
import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreModelService {
    StoreModelDto createStoreModel(CreateStoreModelRequest request);
    StoreModelDto getStoreModelById(Long id);
    List<StoreModelDto> getAllStoreModels();
    Page<StoreModelDto> getStoreModelsByWarehouseAndStatus(Pageable pageable, Long warehouseId, StoreModelStatus status);
    void deleteStoreModel(Long id);

    void updateStatusForStoreModel(Long id, StoreModelStatus status);
}
