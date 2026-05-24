package com.tmdt.shop_service.modules.attaches.domain.repo;

import com.tmdt.shop_service.modules.attaches.domain.AttachType;
import com.tmdt.shop_service.modules.attaches.domain.model.Attaches;

import java.util.List;

public interface AttachRepository {
    Attaches save(Attaches attach);

    List<Attaches> findByInId(List<Long> ids);

    void saveAll(List<Attaches> attaches);

    List<Attaches> findAttachByEntity(Long entityId, AttachType type);
}
