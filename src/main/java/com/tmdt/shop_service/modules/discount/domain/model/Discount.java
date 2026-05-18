package com.tmdt.shop_service.modules.discount.domain.model;

import com.tmdt.shop_service.core.entity.AuditableEntity;
import com.tmdt.shop_service.modules.discount.domain.DiscountType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "discount")
public class Discount extends AuditableEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "module_id")
    private Long moduleId;

    @Column(name = "type", nullable = false)
    @Convert(converter = DiscountType.DiscountTypeConverter.class)
    private DiscountType type;

    @Column(name = "expiry_at")
    private LocalDateTime expiryAt;

    @Column(name = "is_active", nullable = false)
    private Integer isActive = 1;

    public boolean hasUse(Long userId, Long productId) {
        if (this.userId != null && !this.userId.equals(userId)) {
            return false;
        }
        if  (this.moduleId != null && !this.moduleId.equals(productId)) {
            return false;
        }
        if (this.quantity != null && this.quantity <= 0) {
            return false;
        }
        return this.expiryAt == null || !this.expiryAt.isBefore(LocalDateTime.now());
    }
}


