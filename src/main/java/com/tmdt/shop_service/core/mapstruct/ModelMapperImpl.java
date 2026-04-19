package com.tmdt.shop_service.core.mapstruct;

import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ModelMapperImpl<E, D> implements ModelMapper<E, D> {
    @Override
    public List<D> toDtoList(List<E> entities) {
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<E> toEntityList(List<D> dtos) {
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public void updateEntityFromDto(D dto, @MappingTarget E entity) {
        // Default implementation (can be overridden)
    }
}
