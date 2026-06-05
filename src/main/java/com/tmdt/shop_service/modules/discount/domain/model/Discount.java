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
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "user_ids", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Long> userIds = new ArrayList<>();

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "module_ids", columnDefinition = "JSONB")
    @JdbcTypeCode(SqlTypes.JSON)
    private List<Long> moduleIds;

    @Column(name = "type", nullable = false)
    @Convert(converter = DiscountType.DiscountTypeConverter.class)
    private DiscountType type;

    @Column(name = "expiry_from")
    private LocalDateTime expiryFrom;

    @Column(name = "expiry_to")
    private LocalDateTime expiryTo;

    @Column(name = "is_active", nullable = false)
    private Integer isActive = 1;

    @Column(name = "value", nullable = false)
    private Long value;

    public boolean hasUse(Long userId, Long productId) {
        if (this.userIds != null && !this.userIds.isEmpty() && !this.userIds.contains(userId)) {
            return false;
        }
        if  (this.moduleIds != null && !this.moduleIds.isEmpty() && !this.moduleIds.contains(productId)) {
            return false;
        }
        if (this.quantity != null && this.quantity <= 0) {
            return false;
        }
        if (this.isActive != null && this.isActive == 0) {
            return false;
        }
        if (this.expiryFrom != null && this.expiryFrom.isAfter(LocalDateTime.now())) {
            return false;
        }
        if (this.expiryTo != null && this.expiryTo.isBefore(LocalDateTime.now())) {
            return false;
        }
        return true;
    }
}


