package com.tmdt.shop_service.modules.order.domain;

import com.tmdt.shop_service.core.enumtype.EnumConverter;
import com.tmdt.shop_service.core.enumtype.HasEnumValue;
import lombok.Getter;

import java.util.Objects;

@Getter
public enum ProcessStatus implements HasEnumValue {
    MOI(0),
    DANG_CHUAN_BI(1),
    DANG_VAN_CHUYEN(2),
    HOAN_THANH(3),
    HUY(4);

    private final Integer value;
    ProcessStatus(Integer value) {
        this.value = value;
    }

    public final static class ProcessStatusConverter implements EnumConverter<ProcessStatus> {

        @Override
        public Integer convertToDatabaseColumn(ProcessStatus attribute) {
            if (attribute == null) return null;
            return attribute.getValue();
        }

        @Override
        public ProcessStatus convertToEntityAttribute(Integer value) {
            for (ProcessStatus paymentType : ProcessStatus.values()) {
                if (Objects.equals(paymentType.value, value)) return paymentType;
            }
            return null;
        }
    }
}
