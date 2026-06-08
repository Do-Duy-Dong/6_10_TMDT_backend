package com.tmdt.shop_service.modules.payment.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SepayDto {
    String merchant;

    String currency = "VND";

    @JsonProperty("order_amount")
    BigDecimal orderAmount;

    Operation operation;

    @JsonProperty("order_description")
    String orderDescription;

    @JsonProperty("order_invoice_number")
    String orderInvoiceNumber;

    @JsonProperty("customer_id")
    Long customerId;

    @JsonProperty("success_url")
    String successUrl;

    @JsonProperty("error_url")
    String errorUrl;

    @JsonProperty("cancel_url")
    String cancelUrl;

    String signature;
}
