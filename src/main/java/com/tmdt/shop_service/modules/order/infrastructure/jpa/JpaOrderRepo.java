package com.tmdt.shop_service.modules.order.infrastructure.jpa;

import com.tmdt.shop_service.modules.order.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaOrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByOwnerId(Long ownerId);
}
