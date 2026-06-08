package com.tmdt.shop_service.modules.payment.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmdt.shop_service.core.exception.ResourceNotFoundException;
import com.tmdt.shop_service.modules.order.application.dto.OrderDto;
import com.tmdt.shop_service.modules.order.application.service.OrderService;
import com.tmdt.shop_service.modules.order.domain.PaymentStatus;
import com.tmdt.shop_service.modules.order.domain.PaymentType;
import com.tmdt.shop_service.modules.order.domain.model.PaymentDetail;
import com.tmdt.shop_service.modules.order.domain.repo.PaymentDetailRepo;
import com.tmdt.shop_service.modules.payment.application.dto.Operation;
import com.tmdt.shop_service.modules.payment.infrastructure.config.SepayConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SepayService implements PaymentService{
    final SepayConfig sepayConfig;
    final OrderService orderService;
    final PaymentDetailRepo paymentDetailRepo;
    final ObjectMapper objectMapper;
    final CloseableHttpClient httpClient;
    private final List<String> ALLOWED_FIELDS = Arrays.asList(
            "order_amount", "merchant", "currency", "operation",
            "order_description", "order_invoice_number", "customer_id",
            "payment_method", "success_url", "error_url", "cancel_url"
    );

    @Override
    public Map<String, String> getPaymentObject(Long orderId, Long userId) {
        OrderDto orderDto = orderService.getOrderDetails(userId, orderId);
        if (!Objects.equals(orderDto.getPaymentType(), PaymentType.QR)) {
            throw new IllegalArgumentException("Đơn hàng này không thể thanh toán QR");
        }
        UUID paymentID = UUID.randomUUID();
        PaymentDetail paymentDetail = paymentDetailRepo.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Có lỗi trong hệ thống hãy liên hệ quản trị viên để hỗ trợ"));

        if (!(Objects.equals(paymentDetail.getStatus(), PaymentStatus.FAIL)
                || Objects.equals(paymentDetail.getStatus(), PaymentStatus.NEW))) {
            throw new IllegalArgumentException("Bạn chưa thể thanh toán đơn này. Hãy thử lại sau");
        }
        paymentDetail.setPaymentUUID(paymentID);
        paymentDetail.setStatus(PaymentStatus.PENDING);
        paymentDetailRepo.save(paymentDetail);

        return getPaymentUrl(orderDto.getOwnerId(), orderDto.getTotal(), paymentID);
    }

    /**
     *  Mock payment
     */
    @Override
    public Map<String, String> getPaymentUrl(Long customerId, BigDecimal total, UUID paymentUUID) {
        try {
            Map<String, String> rawValue = new HashedMap<>();
            rawValue.put("order_amount", total.toString());
            rawValue.put("merchant", sepayConfig.getMerchantId());
            rawValue.put("currency", "VND");
            rawValue.put("operation", Operation.PURCHASE.toString());
            rawValue.put("order_description", "Thanh toán đơn hàng #" + paymentUUID);
            rawValue.put("order_invoice_number", paymentUUID.toString());
            rawValue.put("customer_id", customerId.toString());
            rawValue.put("success_url", "http://localhost:3000/public/laptops");

            String signed = genSignedString(rawValue, sepayConfig.getSecretKey());
            rawValue.put("signature", signed);

            return rawValue;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    private String genSignedString(Map<String, String> rawData, String secretKey) {
        try {
            List<String> signedFields = new LinkedList<>();

            for (String field : ALLOWED_FIELDS) {
                String value = rawData.get(field);
                if (value != null && !value.isEmpty()) {
                    signedFields.add(field + "=" + value);
                }
            }

            String signedString = String.join(",", signedFields);

            return hashHmacSha256ToBase64(signedString, secretKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("");
        }
    }

    private String hashHmacSha256ToBase64(String data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {

        String algorithm = "HmacSHA256";
        Mac sha256Hmac = Mac.getInstance(algorithm);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
        sha256Hmac.init(secretKeySpec);

        byte[] rawHmac = sha256Hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(rawHmac);
    }

    @Override
    public void handlePaymentIpn(Map<String, Object> ipnReturn) {
        try {
            Map<String, Object> order = (Map<String, Object>) ipnReturn.get("order");
            if (order != null) {
                String paymentUUID = order.get("order_invoice_number").toString();
                String orderNumber = order.get("order_amount").toString();
                PaymentDetail paymentDetail = paymentDetailRepo.findByPaymentUUID(UUID.fromString(paymentUUID))
                        .orElseThrow(() -> new ResourceNotFoundException("Payment with UUID " + paymentUUID +" not found: "));
                OrderDto orderDto = orderService.getOrderDetails(paymentDetail.getOrderId());

                BigDecimal amountPaid = new BigDecimal(orderNumber);
                if (amountPaid.compareTo(orderDto.getTotal()) != -1) {
                    paymentDetail.setStatus(PaymentStatus.SUCCESS);
                    paymentDetailRepo.save(paymentDetail);
                } else {
                    paymentDetail.setStatus(PaymentStatus.FAIL);
                    paymentDetailRepo.save(paymentDetail);
                    throw new IllegalArgumentException("Số tiền thanh toán không hợp lệ");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
