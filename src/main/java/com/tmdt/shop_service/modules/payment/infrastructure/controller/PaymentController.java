package com.tmdt.shop_service.modules.payment.infrastructure.controller;

import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import com.tmdt.shop_service.modules.payment.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    final PaymentService paymentService;

    @GetMapping("/{orderId}")
    public Map<String, String> getUrl(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long orderId) {
        return paymentService.getPaymentObject(orderId, userDetail.getId());
    }

    @GetMapping
    public Map<String, String> mockPayment() {
        return paymentService.getPaymentUrl(1L, BigDecimal.valueOf(10000l), UUID.randomUUID());
    }

    @PostMapping("/sepay/ipn")
    public ResponseEntity sepayIpn(@RequestBody(required = false) Map<String, Object> objects) {
        paymentService.handlePaymentIpn(objects);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
