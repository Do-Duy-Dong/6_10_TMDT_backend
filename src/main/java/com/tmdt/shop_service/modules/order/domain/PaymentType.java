package com.tmdt.shop_service.modules.order.domain;

import com.tmdt.shop_service.core.enumtype.EnumConverter;
import com.tmdt.shop_service.core.enumtype.HasEnumValue;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum PaymentType implements HasEnumValue {
    COD(0), //Giao tận nhà
    QR(1); // Nhận tại cửa hàng

    private final Integer value;
    PaymentType(Integer value) {
        this.value = value;
    }

    public final static class PaymentTypeConverter implements EnumConverter<PaymentType> {

        @Override
        public Integer convertToDatabaseColumn(PaymentType attribute) {
            if (attribute == null) return null;
            return attribute.getValue();
        }

        @Override
        public PaymentType convertToEntityAttribute(Integer value) {
            for (PaymentType paymentType : PaymentType.values()) {
                if (Objects.equals(paymentType.value, value)) return paymentType;
            }
            return null;
        }
    }
}
