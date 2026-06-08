package com.tmdt.shop_service.modules.export.domain.model;

import com.tmdt.shop_service.core.enumtype.HasEnumValue;
import lombok.Getter;

@Getter
public enum ReportType implements HasEnumValue {
    BAO_CAO_DOANH_THU(1),
    BAO_CAO_TON_KHO(2),
    TON_KHO_HIEN_TAI(3);

    private final Integer value;

    ReportType(Integer value) {
        this.value = value;
    }
}
