package com.tmdt.shop_service.modules.order.domain.repo;

import com.tmdt.shop_service.modules.order.domain.model.PaymentDetail;
import java.util.Optional;
import java.util.UUID;

public interface PaymentDetailRepo {
    PaymentDetail save(PaymentDetail paymentDetail);
    Optional<PaymentDetail> findByOrderId(Long orderId);

    Optional<PaymentDetail> findByPaymentUUID(UUID paymentUUID);
}
