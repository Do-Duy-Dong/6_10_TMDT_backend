package com.tmdt.shop_service.modules.order.application.service;

import com.tmdt.shop_service.modules.order.application.dto.OrderDto;
import com.tmdt.shop_service.modules.order.application.request.OrderRequest;
import com.tmdt.shop_service.modules.order.domain.PaymentStatus;
import com.tmdt.shop_service.modules.order.domain.ProcessStatus;
import com.tmdt.shop_service.modules.order.domain.ShipmentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    OrderDto placeOrder(Long userId, OrderRequest request);
    List<OrderDto> getMyOrders(Long userId);
    OrderDto getOrderDetails(Long userId, Long orderId);
    OrderDto getOrderDetails(Long orderId);

    // Admin features
    List<OrderDto> getAllOrders();
    OrderDto updateProcessStatus(Long adminId, Long orderId, ProcessStatus status);

    Page<OrderDto> getOrderByParams(
            Pageable pageable,
            List<PaymentStatus> statusIn,
            List<ProcessStatus> processStatusIn,
            ShipmentType shipmentTypeEq,
            String emailEq);

    OrderDto getOrderDetailByOrderId(Long orderId);
}
