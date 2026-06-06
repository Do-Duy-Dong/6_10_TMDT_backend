package com.tmdt.shop_service.modules.export.domain.model;

import com.tmdt.shop_service.core.enumtype.HasEnumValue;
import lombok.Getter;

@Getter
public enum ReportType implements HasEnumValue {
    BAO_CAO_DOANH_THU(1);

    private final Integer value;

    ReportType(Integer value) {
        this.value = value;
    }
}
