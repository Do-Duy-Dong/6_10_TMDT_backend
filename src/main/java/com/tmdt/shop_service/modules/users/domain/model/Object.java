package com.tmdt.shop_service.modules.users.domain.model;

import com.tmdt.shop_service.core.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "objects", uniqueConstraints = @UniqueConstraint(columnNames = {"code"}))
public class Object extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "is_active", nullable = false)
    private Integer isActive;
}
