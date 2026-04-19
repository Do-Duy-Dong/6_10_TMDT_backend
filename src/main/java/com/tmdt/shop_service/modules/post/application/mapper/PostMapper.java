package com.tmdt.shop_service.modules.post.application.mapper;

import com.tmdt.shop_service.core.mapstruct.ModelMapper;
import com.tmdt.shop_service.modules.post.application.dto.PostDto;
import com.tmdt.shop_service.modules.post.domain.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostMapper extends ModelMapper<Post, PostDto> {
    public static final PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);
}
