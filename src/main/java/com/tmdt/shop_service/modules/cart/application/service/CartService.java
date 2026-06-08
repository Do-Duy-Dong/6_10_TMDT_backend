package com.tmdt.shop_service.modules.cart.application.service;

import com.tmdt.shop_service.modules.cart.application.dto.CartDto;
import com.tmdt.shop_service.modules.cart.application.request.CartRequest;

import java.util.List;

public interface CartService {
    CartDto addToCart(Long ownerId, CartRequest request);
    CartDto updateCartQuantity(Long ownerId, Long cartId, Integer quantity);
    void removeFromCart(Long ownerId, Long cartId);
    List<CartDto> getCartByOwnerId(Long ownerId);
}
