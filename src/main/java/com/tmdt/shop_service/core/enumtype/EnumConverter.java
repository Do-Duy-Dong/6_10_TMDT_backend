package com.tmdt.shop_service.core.enumtype;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public interface EnumConverter<HasEnumValue> extends AttributeConverter<HasEnumValue, Integer> {
    Integer convertToDatabaseColumn(HasEnumValue attribute);

    HasEnumValue convertToEntityAttribute(Integer value);
}
