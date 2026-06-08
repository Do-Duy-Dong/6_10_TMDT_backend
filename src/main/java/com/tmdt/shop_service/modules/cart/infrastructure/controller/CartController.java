package com.tmdt.shop_service.modules.cart.infrastructure.controller;

import com.tmdt.shop_service.core.dto.CollectionResponse;
import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import com.tmdt.shop_service.modules.cart.application.dto.CartDto;
import com.tmdt.shop_service.modules.cart.application.request.CartRequest;
import com.tmdt.shop_service.modules.cart.application.request.UpdateCartRequest;
import com.tmdt.shop_service.modules.cart.application.service.CartService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/carts")
@RequiredArgsConstructor
@Tag(name = "User API for Cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public CollectionResponse<CartDto> getMyCart(@AuthenticationPrincipal CustomUserDetail userDetail) {
        List<CartDto> result = cartService.getCartByOwnerId(userDetail.getId());
        return new CollectionResponse<>(result, null, (long) result.size());
    }

    @PostMapping
    public ResponseEntity<CartDto> addToCart(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @Valid @RequestBody CartRequest request) {
        CartDto result = cartService.addToCart(userDetail.getId(), request);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<CartDto> updateCartQuantity(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long cartId,
            @Valid @RequestBody UpdateCartRequest request) {
        CartDto result = cartService.updateCartQuantity(userDetail.getId(), cartId, request.getQuantity());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeFromCart(
            @AuthenticationPrincipal CustomUserDetail userDetail,
            @PathVariable Long cartId) {
        cartService.removeFromCart(userDetail.getId(), cartId);
        return ResponseEntity.noContent().build();
    }
}
