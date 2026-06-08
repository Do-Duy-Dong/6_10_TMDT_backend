package com.tmdt.shop_service.modules.warehouse.domain.model;

import com.tmdt.shop_service.core.entity.AuditableEntity;
import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "store_model")
public class StoreModel extends AuditableEntity {
    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "serial_number", nullable = false)
    private String serialNumber;

    @Column(name = "laptop_id", nullable = false)
    private Long laptopId;

    @Column(name = "status")
    @Convert(converter = StoreModelStatus.StoreModelStatusConverter.class)
    private StoreModelStatus status;

    @Column(name = "option_id", nullable = false)
    private Long optionId;
}
