package com.tmdt.shop_service.modules.discount.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.discount.application.dto.DiscountDto;
import com.tmdt.shop_service.modules.discount.domain.model.Discount;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DiscountMapper extends ModelMapper<Discount, DiscountDto> {
    public static final DiscountMapper INSTANCE = Mappers.getMapper(DiscountMapper.class);
}
