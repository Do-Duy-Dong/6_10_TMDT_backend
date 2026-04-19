package com.tmdt.shop_service.core.mapstruct;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface ModelMapper<E, D> {

    D toDto(E entity); // Convert Entity -> DTO

    E toEntity(D dto); // Convert DTO -> Entity

    List<D> toDtoList(List<E> entities); // Convert List<Entity> -> List<DTO>

    List<E> toEntityList(List<D> dtos); // Convert List<DTO> -> List<Entity>

    void updateEntityFromDto(D dto, @MappingTarget E entity); // Update existing Entity from DTO
}
