package com.tmdt.shop_service.modules.cart.infrastructure.jpa;

import com.tmdt.shop_service.modules.cart.domain.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaCartRepo extends JpaRepository<Cart, Long> {
    Optional<Cart> findByOwnerIdAndOptionId(Long ownerId, Long optionId);
    List<Cart> findByOwnerId(Long ownerId);
}
