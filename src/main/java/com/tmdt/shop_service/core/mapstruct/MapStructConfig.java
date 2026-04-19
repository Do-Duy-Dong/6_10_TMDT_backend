package com.tmdt.shop_service.core.mapstruct;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface MapStructConfig {
    @Named("fromJsonToMap")
    default Map<String, Object> fromJsonToMap(String originJsonString) throws IOException {
        if (Objects.nonNull(originJsonString)) {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(originJsonString, new TypeReference<>() {});
        }
        return null;
    }
}