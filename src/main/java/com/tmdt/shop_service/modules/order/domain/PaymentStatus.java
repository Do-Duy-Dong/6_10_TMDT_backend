package com.tmdt.shop_service.modules.order.domain;

import com.tmdt.shop_service.core.enumtype.EnumConverter;
import com.tmdt.shop_service.core.enumtype.HasEnumValue;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum PaymentStatus implements HasEnumValue {
    COD(0), //Giao tận nhà
    NEW(1),
    PENDING(2),
    FAIL(3),
    SUCCESS(4);

    private final Integer value;
    PaymentStatus(Integer value) {
        this.value = value;
    }

    public final static class PaymentStatusConverter implements EnumConverter<PaymentStatus> {

        @Override
        public Integer convertToDatabaseColumn(PaymentStatus attribute) {
            if (attribute == null) return null;
            return attribute.getValue();
        }

        @Override
        public PaymentStatus convertToEntityAttribute(Integer value) {
            for (PaymentStatus paymentStatus : PaymentStatus.values()) {
                if (Objects.equals(paymentStatus.value, value)) return paymentStatus;
            }
            return null;
        }
    }
}
