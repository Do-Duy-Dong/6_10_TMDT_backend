package com.tmdt.shop_service.modules.cart.domain.repo;

import com.tmdt.shop_service.modules.cart.application.dto.CartDto;
import com.tmdt.shop_service.modules.cart.domain.model.Cart;
import java.util.List;
import java.util.Optional;

public interface CartRepo {
    Cart save(Cart cart);
    Optional<Cart> findById(Long id);
    Optional<Cart> findByOwnerIdAndOptionId(Long ownerId, Long optionId);
    List<CartDto> findByOwnerId(Long ownerId);
    void delete(Long id);
}
