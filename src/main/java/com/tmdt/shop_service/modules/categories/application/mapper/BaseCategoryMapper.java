package com.tmdt.shop_service.modules.categories.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.categories.application.dto.BaseCategoryDto;
import com.tmdt.shop_service.modules.categories.domain.model.BaseCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BaseCategoryMapper extends ModelMapper<BaseCategory, BaseCategoryDto> {
    public static final BaseCategoryMapper INSTANCE = Mappers.getMapper(BaseCategoryMapper.class);
}
