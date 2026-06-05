package com.tmdt.shop_service.modules.order.domain.model;

import com.tmdt.shop_service.core.entity.AuditableEntity;
import com.tmdt.shop_service.modules.order.domain.PaymentType;
import com.tmdt.shop_service.modules.order.domain.ShipmentType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order extends AuditableEntity {
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "discount_id")
    private Long discountId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "shipment_type", nullable = false)
    @Convert(converter = ShipmentType.ShipmentTypeConverter.class)
    private ShipmentType shipmentType;

    @Column(name = "payment_type", nullable = false)
    @Convert(converter = PaymentType.PaymentTypeConverter.class)
    private PaymentType paymentType;

    @Column(name = "district", nullable = false)
    private String district;

    @Column(name = "province", nullable = false)
    private String province;

    @Column(name = "commune", nullable = false)
    private String commune;

    @Column(name = "address_detail", nullable = false)
    private String addressDetail;

    @Column(name = "total", nullable = false)
    private BigDecimal total;
}
