package com.tmdt.shop_service.modules.attaches.application.service;

import com.tmdt.shop_service.modules.attaches.application.dto.AttachDto;
import com.tmdt.shop_service.modules.attaches.application.request.CreateAttachRequest;
import com.tmdt.shop_service.modules.attaches.domain.AttachType;

import java.util.List;

public interface AttachService {
    AttachDto create(CreateAttachRequest request, Long ownerId);

    List<AttachDto> assignAttachesForEntity(List<Long> attachIds, Long entityId, AttachType entityType);
}
