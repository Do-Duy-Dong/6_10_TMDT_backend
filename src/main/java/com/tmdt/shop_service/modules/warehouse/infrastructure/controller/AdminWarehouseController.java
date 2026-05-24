package com.tmdt.shop_service.modules.warehouse.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.warehouse.application.dto.WarehouseDto;
import com.tmdt.shop_service.modules.warehouse.application.request.CreateWarehouseRequest;
import com.tmdt.shop_service.modules.warehouse.application.request.UpdateWarehouseRequest;
import com.tmdt.shop_service.modules.warehouse.application.service.WarehouseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/warehouses")
@RequiredArgsConstructor
public class AdminWarehouseController {
    private final WarehouseService warehouseService;

    @PostMapping
    public ResponseEntity<WarehouseDto> createWarehouse(@RequestBody @Valid CreateWarehouseRequest request) {
        return ResponseEntity.ok(warehouseService.createWarehouse(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseDto> updateWarehouse(@PathVariable Long id, @RequestBody UpdateWarehouseRequest request) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseDto> getWarehouseById(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(id));
    }

    @GetMapping
    public CollectionResponse<WarehouseDto> getAllWarehouses(
            @PageableDefault(size = 10, page = 0, sort = "create_at", direction = Sort.Direction.DESC)
            Pageable pageable,
            @RequestParam(name = "name:ct", required = false) String nameCt,
            @RequestParam(name = "isActive", required = false) Integer isActive) {
        Page result = warehouseService.getAllWarehousesByParams(pageable, nameCt, isActive);
        Integer nextPageToken = result.hasNext() ? result.getNumber() + 1 : null;
        return new CollectionResponse<WarehouseDto>(
                result.getContent(),
                nextPageToken,
                result.getTotalElements());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}
