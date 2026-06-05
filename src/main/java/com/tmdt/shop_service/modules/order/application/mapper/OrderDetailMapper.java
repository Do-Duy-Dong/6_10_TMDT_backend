package com.tmdt.shop_service.modules.order.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.order.application.dto.OrderDetailDto;
import com.tmdt.shop_service.modules.order.domain.model.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrderDetailMapper extends ModelMapper<OrderDetail, OrderDetailDto> {
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);
}
