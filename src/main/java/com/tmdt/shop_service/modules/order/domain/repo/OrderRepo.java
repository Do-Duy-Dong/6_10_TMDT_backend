package com.tmdt.shop_service.modules.order.domain.repo;

import com.tmdt.shop_service.modules.order.application.dto.OrderDto;
import com.tmdt.shop_service.modules.order.domain.PaymentStatus;
import com.tmdt.shop_service.modules.order.domain.ProcessStatus;
import com.tmdt.shop_service.modules.order.domain.ShipmentType;
import com.tmdt.shop_service.modules.order.domain.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderRepo {
    Order save(Order order);
    Optional<Order> findById(Long id);
    List<Order> findByOwnerId(Long ownerId);
    List<Order> findAll();

    Page<OrderDto> getOrderByParams(
            Pageable pageable,
            List<PaymentStatus> statusIn,
            List<ProcessStatus> processStatusIn,
            ShipmentType shipmentTypeEq,
            String emailEq);
}
