package com.tmdt.shop_service.modules.warehouse.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.warehouse.application.dto.WarehouseDto;
import com.tmdt.shop_service.modules.warehouse.domain.model.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WarehouseMapper extends ModelMapper<Warehouse, WarehouseDto> {
    WarehouseMapper INSTANCE = Mappers.getMapper(WarehouseMapper.class);
}
