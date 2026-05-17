package com.tmdt.shop_service.modules.categories.domain.model;

import com.tmdt.shop_service.core.entity.AuditableEntity;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.persistence.Column;
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
@Table(name = "base_category")
public class BaseCategory extends AuditableEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "is_active", nullable = false)
    private Integer isActive;
}
