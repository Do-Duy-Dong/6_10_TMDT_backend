package com.tmdt.shop_service.modules.payment.application.service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface PaymentService {
    Map<String, String> getPaymentObject(Long orderId, Long userId);

    Map<String, String> getPaymentUrl(Long customerId, BigDecimal total, UUID paymentUUID);

    void handlePaymentIpn(Map<String, Object> ipnReturn);
}
