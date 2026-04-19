package com.tmdt.shop_service.modules.categories.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.categories.application.dto.CategoryDto;
import com.tmdt.shop_service.modules.categories.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends ModelMapper<Category, CategoryDto> {
    public static final CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);
}
