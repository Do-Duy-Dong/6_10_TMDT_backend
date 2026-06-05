package com.tmdt.shop_service.modules.order.infrastructure.jpa;

import com.tmdt.shop_service.modules.order.domain.model.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPaymentDetailRepo extends JpaRepository<PaymentDetail, Long> {
    Optional<PaymentDetail> findByOrderId(Long orderId);

    Optional<PaymentDetail> findPaymentDetailByPaymentUUID(UUID paymentUUID);
}
