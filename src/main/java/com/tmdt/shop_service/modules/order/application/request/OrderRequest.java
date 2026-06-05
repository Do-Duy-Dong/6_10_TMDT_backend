package com.tmdt.shop_service.modules.order.application.request;

import com.tmdt.shop_service.modules.order.domain.PaymentType;
import com.tmdt.shop_service.modules.order.domain.ShipmentType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    @NotNull(message = "Full name must not be null")
    private String fullName;

    @NotNull(message = "Phone number must not be null")
    private String phoneNumber;

    private String email;

    @NotNull(message = "Shipment type must not be null")
    private ShipmentType shipmentType;

    @NotNull(message = "Payment type must not be null")
    private PaymentType paymentType;

    @NotNull(message = "District must not be null")
    private String district;

    @NotNull(message = "Province must not be null")
    private String province;

    @NotNull(message = "Commune must not be null")
    private String commune;

    @NotNull(message = "Address detail must not be null")
    private String addressDetail;

    private Long discountId;

    @NotEmpty(message = "Cart item IDs must not be empty")
    private List<Long> cartIds;
}
