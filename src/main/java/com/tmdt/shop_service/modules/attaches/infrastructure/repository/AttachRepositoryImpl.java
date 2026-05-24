package com.tmdt.shop_service.modules.attaches.infrastructure.repository;

import com.tmdt.shop_service.modules.attaches.domain.AttachType;
import com.tmdt.shop_service.modules.attaches.domain.model.Attaches;
import com.tmdt.shop_service.modules.attaches.domain.repo.AttachRepository;
import com.tmdt.shop_service.modules.attaches.infrastructure.jpa.JpaAttachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttachRepositoryImpl implements AttachRepository {
    final JpaAttachRepository jpaAttachRepository;

    @Override
    public Attaches save(Attaches attach) {
        return jpaAttachRepository.save(attach);
    }

    @Override
    public List<Attaches> findByInId(List<Long> ids) {
        return jpaAttachRepository.findByIdIn(ids);
    }

    @Override
    public void saveAll(List<Attaches> attaches) {
        jpaAttachRepository.saveAll(attaches);
    }

    @Override
    public List<Attaches> findAttachByEntity(Long entityId, AttachType type) {
        return jpaAttachRepository.findByModuleIdAndType(entityId, type);
    }

    @Override
    public List<Attaches> findAttachByEntities(List<Long> entityIds, AttachType type) {
        return jpaAttachRepository.findByModuleIdInAndTypeOrderByCreateAtDesc(entityIds, type);
    }

    @Override
    public void deleteAll(List<Attaches> attaches) {
        jpaAttachRepository.deleteAll(attaches);
    }
}
