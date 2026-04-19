package com.tmdt.shop_service.modules.laptop.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.domain.model.Laptop;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LaptopMapper extends ModelMapper<Laptop, LaptopDto> {
    public static final LaptopMapper INSTANCE = Mappers.getMapper(LaptopMapper.class);
}
