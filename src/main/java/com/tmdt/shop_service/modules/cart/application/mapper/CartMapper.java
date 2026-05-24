package com.tmdt.shop_service.modules.cart.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.cart.application.dto.CartDto;
import com.tmdt.shop_service.modules.cart.domain.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CartMapper extends ModelMapper<Cart, CartDto> {
    CartMapper INSTANCE = Mappers.getMapper(CartMapper.class);
}
