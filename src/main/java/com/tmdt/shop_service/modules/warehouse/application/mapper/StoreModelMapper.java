package com.tmdt.shop_service.modules.warehouse.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.warehouse.application.dto.StoreModelDto;
import com.tmdt.shop_service.modules.warehouse.domain.model.StoreModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StoreModelMapper extends ModelMapper<StoreModel, StoreModelDto> {
    StoreModelMapper INSTANCE = Mappers.getMapper(StoreModelMapper.class);
}
