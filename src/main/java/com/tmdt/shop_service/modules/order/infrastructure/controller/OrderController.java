package com.tmdt.shop_service.modules.order.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import com.tmdt.shop_service.modules.order.application.dto.OrderDto;
import com.tmdt.shop_service.modules.order.application.request.OrderRequest;
import com.tmdt.shop_service.modules.order.application.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/orders")
@RequiredArgsConstructor
@Tag(name = "User API for Order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @Valid @RequestBody OrderRequest request) {
        OrderDto result = orderService.placeOrder(userDetail.getId(), request);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public CollectionResponse<OrderDto> getMyOrders(@AuthenticationPrincipal CustomUserDetail userDetail) {
        List<OrderDto> result = orderService.getMyOrders(userDetail.getId());
        return new CollectionResponse<>(result, null, (long) result.size());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderDetails(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long orderId) {
        OrderDto result = orderService.getOrderDetails(userDetail.getId(), orderId);
        return ResponseEntity.ok(result);
    }
}
