package com.tmdt.shop_service.modules.order.domain.model;

import com.tmdt.shop_service.core.entity.AuditableEntity;
import com.tmdt.shop_service.modules.order.domain.PaymentType;
import com.tmdt.shop_service.modules.order.domain.ProcessStatus;
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
@Table(name = "process")
public class Process extends AuditableEntity {
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "status", nullable = false)
    @Convert(converter = ProcessStatus.ProcessStatusConverter.class)
    private ProcessStatus status;

    @Column(name = "update_by", nullable = false)
    private Long updateBy;
}
