package com.tmdt.shop_service.modules.attaches.application.service;

import com.tmdt.shop_service.modules.attaches.domain.repo.AttachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttachServiceImpl implements AttachService {
    final AttachRepository auditRepository;
}
