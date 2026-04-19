package com.tmdt.shop_service.modules.categories.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.categories.application.dto.CategoryDto;
import com.tmdt.shop_service.modules.categories.application.dto.ModuleCategoryDto;
import com.tmdt.shop_service.modules.categories.domain.model.Category;
import com.tmdt.shop_service.modules.categories.domain.model.ModuleCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ModuleCategoryMapper extends ModelMapper<ModuleCategory, ModuleCategoryDto> {
    public static final ModuleCategoryMapper INSTANCE = Mappers.getMapper(ModuleCategoryMapper.class);
}
