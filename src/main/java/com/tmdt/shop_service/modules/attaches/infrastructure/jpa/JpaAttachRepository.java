package com.tmdt.shop_service.modules.attaches.infrastructure.jpa;

import com.tmdt.shop_service.modules.attaches.domain.model.Attaches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface JpaAttachRepository extends JpaRepository<Attaches, Long> {
    List<Attaches> findByIdIn(Collection<Long> ids);
}
