package com.tmdt.shop_service.modules.warehouse.application.service;

import com.tmdt.shop_service.core.exception.ResourceNotFoundException;
import com.tmdt.shop_service.modules.warehouse.application.dto.StoreModelDto;
import com.tmdt.shop_service.modules.warehouse.application.mapper.StoreModelMapper;
import com.tmdt.shop_service.modules.warehouse.application.request.CreateStoreModelRequest;
import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import com.tmdt.shop_service.modules.warehouse.domain.model.StoreModel;
import com.tmdt.shop_service.modules.warehouse.domain.repo.StoreModelRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StoreModelServiceImpl implements StoreModelService {
    private final StoreModelRepo storeModelRepo;
    private final StoreModelMapper storeModelMapper;

    @Override
    public StoreModelDto createStoreModel(CreateStoreModelRequest request) {
        if (storeModelRepo.findBySerialNumber(request.getSerialNumber()).isPresent()) {
            throw new RuntimeException("Serial number already exists");
        }
        
        StoreModel storeModel = new StoreModel();
        storeModel.setWarehouseId(request.getWarehouseId());
        storeModel.setSerialNumber(request.getSerialNumber());
        storeModel.setLaptopId(request.getLaptopId());
        storeModel.setStatus(request.getStatus());
        
        return storeModelMapper.toDto(storeModelRepo.save(storeModel));
    }

    @Override
    @Transactional(readOnly = true)
    public StoreModelDto getStoreModelById(Long id) {
        return storeModelRepo.findById(id)
                .map(storeModelMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Store model not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StoreModelDto> getAllStoreModels() {
        return storeModelMapper.toDtoList(storeModelRepo.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<StoreModelDto> getStoreModelsByWarehouseAndStatus(
            Pageable pageable,
            Long warehouseId,
            StoreModelStatus status) {
        var result = storeModelRepo.findByWarehouseIdAndStatus(pageable, warehouseId, status);
        return new PageImpl<>(
                StoreModelMapper.INSTANCE.toDtoList(result.getContent()),
                pageable,
                result.getTotalElements());
    }

    @Override
    public void deleteStoreModel(Long id) {
        storeModelRepo.deleteById(id);
    }

    @Override
    public void updateStatusForStoreModel(Long id, StoreModelStatus status) {
        StoreModel storeModel = storeModelRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store model not found"));

        storeModel.setStatus(status);
        storeModelRepo.save(storeModel);
    }
}
