package com.tmdt.shop_service.modules.order.domain.repo;

import com.tmdt.shop_service.modules.order.domain.model.OrderDetail;
import java.util.List;

public interface OrderDetailRepo {
    OrderDetail save(OrderDetail orderDetail);
    List<OrderDetail> findByOrderId(Long orderId);
}
