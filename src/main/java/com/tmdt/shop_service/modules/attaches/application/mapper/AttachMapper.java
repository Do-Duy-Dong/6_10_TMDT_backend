package com.tmdt.shop_service.modules.attaches.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.attaches.application.dto.AttachDto;
import com.tmdt.shop_service.modules.attaches.domain.model.Attaches;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttachMapper extends ModelMapper<Attaches, AttachDto> {
    public static final AttachMapper INSTANCE = Mappers.getMapper(AttachMapper.class);
}
