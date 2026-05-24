package com.tmdt.shop_service.modules.laptop.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.laptop.application.dto.OptionLaptopDto;
import com.tmdt.shop_service.modules.laptop.domain.model.OptionLaptop;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OptionLaptopMapper extends ModelMapper<OptionLaptop, OptionLaptopDto> {
    public static final OptionLaptopMapper INSTANCE = Mappers.getMapper(OptionLaptopMapper.class);
}
