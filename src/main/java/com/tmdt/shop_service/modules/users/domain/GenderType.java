package com.tmdt.shop_service.modules.users.domain;

import com.tmdt.shop_service.core.enumtype.EnumConverter;
import com.tmdt.shop_service.core.enumtype.HasEnumValue;
import lombok.Getter;

@Getter
public enum GenderType implements HasEnumValue {
    MALE(0),
    FEMALE(1);

    private final Integer value;

    GenderType(Integer value) {
        this.value = value;
    }

    public static final class GenderTypeConverter implements EnumConverter<GenderType> {

        @Override
        public Integer convertToDatabaseColumn(GenderType attribute) {
            return attribute.getValue();
        }

        @Override
        public GenderType convertToEntityAttribute(Integer value) {
            for (GenderType genderType : GenderType.values()) {
                if (genderType.getValue().equals(value)) {
                    return genderType;
                }
            }
            return null;
        }
    }
}
