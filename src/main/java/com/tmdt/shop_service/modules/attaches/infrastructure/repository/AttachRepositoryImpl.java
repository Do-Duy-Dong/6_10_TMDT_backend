package com.tmdt.shop_service.modules.attaches.infrastructure.repository;

import com.tmdt.shop_service.modules.attaches.domain.repo.AttachRepository;
import com.tmdt.shop_service.modules.attaches.infrastructure.jpa.JpaAttachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttachRepositoryImpl implements AttachRepository {
    final JpaAttachRepository jpaAttachRepository;
}
