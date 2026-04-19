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

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users_roles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}, name = "unique_roleId_userId"))
public class UserRole extends BaseEntity {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "role_id")
    private Long roleId;

    @Column(name = "is_active")
    private Integer isActive;
}
