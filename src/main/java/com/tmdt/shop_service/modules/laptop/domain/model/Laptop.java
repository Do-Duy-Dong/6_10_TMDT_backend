package com.tmdt.shop_service.modules.laptop.domain.model;

import com.tmdt.shop_service.core.entity.AuditableEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import jakarta.persistence.Column;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "laptop")
public class Laptop extends AuditableEntity {

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "text", nullable = false)
    private String description;

    @Column(name = "is_active", nullable = false)
    private Integer isActive;

    @Column(name = "create_by", nullable = false)
    private Long createBy;

    @Column(name = "original_price", nullable = false)
    private BigDecimal originalPrice;
}
