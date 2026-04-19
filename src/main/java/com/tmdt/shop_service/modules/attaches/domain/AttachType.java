package com.tmdt.shop_service.modules.attaches.domain;

import com.tmdt.shop_service.core.enumtype.EnumConverter;
import com.tmdt.shop_service.core.enumtype.HasEnumValue;
import lombok.Getter;

@Getter
public enum AttachType implements HasEnumValue {
    LAP_TOP(1),
    USER(2),
    COMMENT(3),
    COMPLAINT(4);

    private final Integer value;

    AttachType(Integer value) {
        this.value = value;
    }

    public static final class AttachTypeConverter implements EnumConverter<AttachType> {

        @Override
        public Integer convertToDatabaseColumn(AttachType attribute) {
            return attribute.getValue();
        }

        @Override
        public AttachType convertToEntityAttribute(Integer value) {
            for (AttachType attachType : AttachType.values()) {
                if (attachType.value.equals(value)) {
                    return attachType;
                }
            }
            return null;
        }
    }
}
