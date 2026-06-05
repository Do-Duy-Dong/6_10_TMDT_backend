package com.tmdt.shop_service.modules.order.infrastructure.repo;

import com.tmdt.shop_service.modules.order.domain.model.OrderDetail;
import com.tmdt.shop_service.modules.order.domain.repo.OrderDetailRepo;
import com.tmdt.shop_service.modules.order.infrastructure.jpa.JpaOrderDetailRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderDetailRepoImpl implements OrderDetailRepo {
    private final JpaOrderDetailRepo jpaOrderDetailRepo;

    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        return jpaOrderDetailRepo.save(orderDetail);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return jpaOrderDetailRepo.findByOrderId(orderId);
    }
}
