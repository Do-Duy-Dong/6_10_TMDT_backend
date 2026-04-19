package com.tmdt.shop_service.modules.attaches.infrastructure.jpa;

import com.tmdt.shop_service.modules.attaches.domain.model.Attaches;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAttachRepository extends JpaRepository<Attaches, Long> {
}
