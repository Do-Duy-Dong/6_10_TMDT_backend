package com.tmdt.shop_service.modules.order.infrastructure.jpa;

import com.tmdt.shop_service.modules.order.domain.model.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaProcessRepo extends JpaRepository<Process, Long> {
    List<Process> findByOrderId(Long orderId);

    @Query(value = "select process.*\n" +
            "from process where process.order_id = :orderId\n" +
            "order by process.create_at desc limit 1", nativeQuery = true)
    Optional<Process> findLastProcessByOrderId(@Param("orderId") Long orderId);
}
