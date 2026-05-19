package com.tmdt.shop_service.modules.discount.domain;

import com.tmdt.shop_service.core.enumtype.EnumConverter;
import com.tmdt.shop_service.core.enumtype.HasEnumValue;
import lombok.Getter;

@Getter
public enum DiscountType implements HasEnumValue {
    PERCENT(0),
    FIXED(1);

    private final Integer value;

    DiscountType(Integer value) {
        this.value = value;
    }

    public static final class DiscountTypeConverter implements EnumConverter<DiscountType> {

        @Override
        public Integer convertToDatabaseColumn(DiscountType attribute) {
            if (attribute == null) {
                return null;
            }
            return attribute.getValue();
        }

        @Override
        public DiscountType convertToEntityAttribute(Integer value) {
            for  (DiscountType discountType : DiscountType.values()) {
                if (discountType.getValue().equals(value)) {
                    return discountType;
                }
            }
            return null;
        }
    }
}
