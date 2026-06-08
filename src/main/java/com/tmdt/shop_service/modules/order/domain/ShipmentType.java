package com.tmdt.shop_service.modules.order.domain;

import com.tmdt.shop_service.core.enumtype.EnumConverter;
import com.tmdt.shop_service.core.enumtype.HasEnumValue;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum ShipmentType implements HasEnumValue {
    GTN(0), //Giao tận nhà
    NTCH(1); // Nhận tại cửa hàng

    private final Integer value;
    ShipmentType(Integer value) {
        this.value = value;
    }

    public final static class ShipmentTypeConverter implements EnumConverter<ShipmentType> {

        @Override
        public Integer convertToDatabaseColumn(ShipmentType attribute) {
            if (attribute == null) return null;
            return attribute.getValue();
        }

        @Override
        public ShipmentType convertToEntityAttribute(Integer value) {
            for (ShipmentType shipmentType : ShipmentType.values()) {
                if (Objects.equals(shipmentType.value, value)) return shipmentType;
            }
            return null;
        }
    }
}
