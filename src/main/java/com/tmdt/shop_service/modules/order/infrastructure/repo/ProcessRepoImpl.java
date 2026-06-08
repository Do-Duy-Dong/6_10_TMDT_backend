package com.tmdt.shop_service.modules.order.infrastructure.repo;

import com.tmdt.shop_service.modules.order.domain.model.Process;
import com.tmdt.shop_service.modules.order.domain.repo.ProcessRepo;
import com.tmdt.shop_service.modules.order.infrastructure.jpa.JpaProcessRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProcessRepoImpl implements ProcessRepo {
    private final JpaProcessRepo jpaProcessRepo;

    @Override
    public Process save(Process process) {
        return jpaProcessRepo.save(process);
    }

    @Override
    public List<Process> findByOrderId(Long orderId) {
        return jpaProcessRepo.findByOrderId(orderId);
    }

    @Override
    public Optional<Process> findLastProcess(Long orderId) {
        return jpaProcessRepo.findLastProcessByOrderId(orderId);
    }
}
