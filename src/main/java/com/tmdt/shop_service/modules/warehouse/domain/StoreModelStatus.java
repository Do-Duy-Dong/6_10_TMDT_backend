package com.tmdt.shop_service.modules.warehouse.domain;

import com.tmdt.shop_service.core.enumtype.EnumConverter;
import com.tmdt.shop_service.core.enumtype.HasEnumValue;
import lombok.Getter;

@Getter
public enum StoreModelStatus implements HasEnumValue {
    NEW(0),
    ORDERED(1),
    SOLD(2),
    REFUND(3);

    private final Integer value;
    StoreModelStatus(Integer value) {
        this.value = value;
    }

    public static final class StoreModelStatusConverter implements EnumConverter<StoreModelStatus> {

        @Override
        public Integer convertToDatabaseColumn(StoreModelStatus attribute) {
            if (attribute == null) {
                return null;
            }
            return attribute.getValue();
        }


        @Override
        public StoreModelStatus convertToEntityAttribute(Integer value) {
            for (StoreModelStatus storeModelStatus : StoreModelStatus.values()) {
                if (storeModelStatus.getValue().equals(value)) {
                    return storeModelStatus;
                }
            }

            return null;
        }
    }
}
