package com.tmdt.shop_service.modules.cart.domain.model;

import com.tmdt.shop_service.core.entity.AuditableEntity;
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
@Table(name = "cart")
public class Cart extends AuditableEntity {
    @Column(name = "option_id", nullable = false)
    private Long optionId;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;
}
