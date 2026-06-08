package com.tmdt.shop_service.modules.order.domain.repo;

import com.tmdt.shop_service.modules.order.domain.model.Process;
import java.util.List;
import java.util.Optional;

public interface ProcessRepo {
    Process save(Process process);
    List<Process> findByOrderId(Long orderId);
    Optional<Process> findLastProcess(Long orderId);
}
