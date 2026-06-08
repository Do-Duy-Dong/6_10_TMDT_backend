package com.tmdt.shop_service.modules.order.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.order.application.dto.OrderDto;
import com.tmdt.shop_service.modules.order.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderMapper extends ModelMapper<Order, OrderDto> {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
}
