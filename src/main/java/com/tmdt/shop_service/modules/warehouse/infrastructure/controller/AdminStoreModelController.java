package com.tmdt.shop_service.modules.warehouse.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.warehouse.application.dto.CountStoreModelResponse;
import com.tmdt.shop_service.modules.warehouse.application.dto.StoreModelDto;
import com.tmdt.shop_service.modules.warehouse.application.request.CreateStoreModelRequest;
import com.tmdt.shop_service.modules.warehouse.application.service.StoreModelService;
import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/store-models")
@RequiredArgsConstructor
public class AdminStoreModelController {
    private final StoreModelService storeModelService;

    @PostMapping
    public ResponseEntity createStoreModel(@RequestBody @Valid CreateStoreModelRequest request) {
        storeModelService.createStoreModel(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreModelDto> getStoreModelById(@PathVariable Long id) {
        return ResponseEntity.ok(storeModelService.getStoreModelById(id));
    }

    @GetMapping
    public CollectionResponse<CountStoreModelResponse> getStoreModels(
            @PageableDefault(size = 10, page = 0, sort = "create_at", direction = Sort.Direction.DESC)
            Pageable pageable,
            @RequestParam(name = "nameLaptop:ct", required = false) String nameLaptopCt,
            @RequestParam(name = "warehouseId:eq", required = false) Long warehouseIdEq,
            @RequestParam(name = "status:in", required = false) List<StoreModelStatus> status) {
        if (status == null) {
            status = List.of(StoreModelStatus.NEW);
        }
        var result = storeModelService.getStoreModelsByParams(
                pageable,
                nameLaptopCt,
                warehouseIdEq,
                status);
        Integer nextPageToken = result.hasNext() ? result.getNumber() + 1 : null;
        return new CollectionResponse<>(
                result.getContent(),
                nextPageToken,
                result.getTotalElements());
    }

    @GetMapping("/warehouse/{warehouseId}")
    public CollectionResponse<StoreModelDto> getStoreModelsByWarehouse(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
            Pageable pageable,
            @PathVariable Long warehouseId,
            @RequestParam(name = "status", defaultValue = "NEW") StoreModelStatus status) {
        var result = storeModelService.getStoreModelsByWarehouseAndStatus(pageable, warehouseId, status);
        Integer nextPageToken = result.hasNext() ? result.getNumber() + 1 : null;
        return new CollectionResponse<>(
                result.getContent(),
                nextPageToken,
                (long) result.getTotalElements());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStoreModel(@PathVariable Long id) {
        storeModelService.deleteStoreModel(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateStatusForStoreModel(
            @PathVariable Long id,
            @RequestParam(name = "status") StoreModelStatus status) {
        storeModelService.updateStatusForStoreModel(id, status);
        return ResponseEntity.noContent().build();
    }
}
