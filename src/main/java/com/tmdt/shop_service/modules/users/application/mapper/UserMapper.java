package com.tmdt.shop_service.modules.users.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import com.tmdt.shop_service.modules.users.domain.model.Users;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper extends ModelMapper<Users, UserDto> {
    public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
}
