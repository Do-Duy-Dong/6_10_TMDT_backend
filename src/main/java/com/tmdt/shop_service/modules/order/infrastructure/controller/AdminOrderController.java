package com.tmdt.shop_service.modules.order.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import com.tmdt.shop_service.modules.order.application.dto.OrderDto;
import com.tmdt.shop_service.modules.order.application.service.OrderService;
import com.tmdt.shop_service.modules.order.domain.PaymentStatus;
import com.tmdt.shop_service.modules.order.domain.ProcessStatus;
import com.tmdt.shop_service.modules.order.domain.ShipmentType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/admin/orders")
@RequiredArgsConstructor
@Tag(name = "Admin API for Order")
public class AdminOrderController {
    private final OrderService orderService;

    @GetMapping
    public CollectionResponse<OrderDto> getAllOrders(
            @ParameterObject
            @PageableDefault(
                    size = 10,
                    page = 0,
                    sort = "create_at",
                    direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(name = "paymentStatus:in", required = false) List<PaymentStatus> statusIn,
            @RequestParam(name = "processStatus:in", required = false) List<ProcessStatus> processStatusIn,
            @RequestParam(name = "shipmentType:eq", required = false) ShipmentType shipmentTypeEq,
            @RequestParam(name = "email:eq", required = false) String emailEq) {
        var result = orderService.getOrderByParams(pageable, statusIn, processStatusIn, shipmentTypeEq, emailEq);
        Integer nextPageToken = result.hasNext() ? result.getNumber() + 1: null;
        return new CollectionResponse<>(result.getContent(), nextPageToken, result.getTotalElements());
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDto> updateProcessStatus(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long orderId,
            @RequestParam ProcessStatus status) {
        OrderDto result = orderService.updateProcessStatus(userDetail.getId(), orderId, status);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderDetails(
            @PathVariable Long orderId) {
        OrderDto result = orderService.getOrderDetailByOrderId(orderId);
        return ResponseEntity.ok(result);
    }
}
