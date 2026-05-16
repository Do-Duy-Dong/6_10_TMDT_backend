package com.tmdt.shop_service.modules.warehouse.domain.model;

import com.tmdt.shop_service.core.entity.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "warehouse")
public class Warehouse extends AuditableEntity {
    @Column(name = "name")
    private String name;

    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "address")
    private String address;
}
