package com.tmdt.shop_service.modules.order.infrastructure.repo;

import com.tmdt.shop_service.modules.order.domain.model.PaymentDetail;
import com.tmdt.shop_service.modules.order.domain.repo.PaymentDetailRepo;
import com.tmdt.shop_service.modules.order.infrastructure.jpa.JpaPaymentDetailRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PaymentDetailRepoImpl implements PaymentDetailRepo {
    private final JpaPaymentDetailRepo jpaPaymentDetailRepo;

    @Override
    public PaymentDetail save(PaymentDetail paymentDetail) {
        return jpaPaymentDetailRepo.save(paymentDetail);
    }

    @Override
    public Optional<PaymentDetail> findByOrderId(Long orderId) {
        return jpaPaymentDetailRepo.findByOrderId(orderId);
    }

    @Override
    public Optional<PaymentDetail> findByPaymentUUID(UUID paymentUUID) {
        return jpaPaymentDetailRepo.findPaymentDetailByPaymentUUID(paymentUUID);
    }
}
