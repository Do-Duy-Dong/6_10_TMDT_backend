package com.tmdt.shop_service.modules.attaches.application.service;

import com.tmdt.shop_service.modules.attaches.application.dto.AttachDto;
import com.tmdt.shop_service.modules.attaches.application.mapper.AttachMapper;
import com.tmdt.shop_service.modules.attaches.application.request.CreateAttachRequest;
import com.tmdt.shop_service.modules.attaches.domain.AttachType;
import com.tmdt.shop_service.modules.attaches.domain.model.Attaches;
import com.tmdt.shop_service.modules.attaches.domain.repo.AttachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttachServiceImpl implements AttachService {
    final AttachRepository attachRepository;

    @Override
    public AttachDto create(CreateAttachRequest request, Long ownerId) {
        Attaches attach = new Attaches();
        attach.setDescription(request.getDescription());
        attach.setName(request.getName());
        attach.setAttachMetadata(request.getAttachMetadata());
        attach.setOwnerId(ownerId);
        attach.setIsActive(1);
        attachRepository.save(attach);

        return AttachMapper.INSTANCE.toDto(attach);
    }

    @Override
    public List<AttachDto> assignAttachesForEntity(List<Long> attachIds, Long entityId, AttachType entityType) {
        List<Attaches> attaches = attachRepository.findByInId(attachIds);
        attaches.forEach(attach -> {
            attach.setModuleId(entityId);
            attach.setType(entityType);
        });
        attachRepository.saveAll(attaches);
        return AttachMapper.INSTANCE.toDtoList(attaches);
    }

    @Override
    public List<AttachDto> getAttachDtoForEntity(Long entityId, AttachType entityType) {
        var result = attachRepository.findAttachByEntity(entityId, entityType);
        return AttachMapper.INSTANCE.toDtoList(result);
    }
}
