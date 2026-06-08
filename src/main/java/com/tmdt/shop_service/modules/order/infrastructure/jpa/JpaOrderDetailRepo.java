package com.tmdt.shop_service.modules.order.infrastructure.jpa;

import com.tmdt.shop_service.modules.order.domain.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaOrderDetailRepo extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);
}
