package com.tmdt.shop_service.modules.order.application.dto;

import com.tmdt.shop_service.modules.discount.application.dto.DiscountDto;
import com.tmdt.shop_service.modules.order.domain.PaymentStatus;
import com.tmdt.shop_service.modules.order.domain.PaymentType;
import com.tmdt.shop_service.modules.order.domain.ProcessStatus;
import com.tmdt.shop_service.modules.order.domain.ShipmentType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderDto {
    private Long id;
    private Long ownerId;
    private Long discountId;
    private DiscountDto discountDto;
    private String fullName;
    private String phoneNumber;
    private String email;
    private ShipmentType shipmentType;
    private PaymentType paymentType;
    private String district;
    private String province;
    private String commune;
    private String addressDetail;
    private BigDecimal total;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<OrderDetailDto> orderDetails;
    private ProcessStatus status;
    private PaymentStatus paymentStatus;
}
