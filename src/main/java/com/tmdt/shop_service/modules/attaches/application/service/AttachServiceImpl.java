package com.tmdt.shop_service.modules.attaches.application.service;

import com.tmdt.shop_service.modules.attaches.application.dto.AttachDto;
import com.tmdt.shop_service.modules.attaches.application.mapper.AttachMapper;
import com.tmdt.shop_service.modules.attaches.application.request.CreateAttachRequest;
import com.tmdt.shop_service.modules.attaches.domain.AttachType;
import com.tmdt.shop_service.modules.attaches.domain.model.Attaches;
import com.tmdt.shop_service.modules.attaches.domain.repo.AttachRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Attaches> currentAttach = attachRepository.findAttachByEntity(entityId, entityType);
        int orderNumber = 0;
        for (Attaches attach: currentAttach) {
            if (attach.getOrderNumber() != null) {
                orderNumber = Math.max(orderNumber, attach.getOrderNumber());
            }
        }
        orderNumber++;
        List<Attaches> attaches = attachRepository.findByInId(attachIds);
        for(Attaches attach : attaches) {
            attach.setModuleId(entityId);
            attach.setType(entityType);
            attach.setOrderNumber(orderNumber);
            orderNumber++;
        }
        attachRepository.saveAll(attaches);
        return AttachMapper.INSTANCE.toDtoList(attaches);
    }

    @Override
    public void detachAttachForEntity(List<Long> attachIds, Long entityId, AttachType entityType) {
        List<Attaches> attaches = attachRepository.findByInId(attachIds);
        attachRepository.deleteAll(attaches);
    }

    @Override
    public List<AttachDto> getAttachDtoForEntity(Long entityId, AttachType entityType) {
        var result = attachRepository.findAttachByEntity(entityId, entityType);
        return AttachMapper.INSTANCE.toDtoList(result);
    }

    @Override
    public List<AttachDto> getAttachDtoForEntities(List<Long> entityIds, AttachType entityType) {
        var result = attachRepository.findAttachByEntities(entityIds, entityType);
        return AttachMapper.INSTANCE.toDtoList(result);
    }

    @Override
    public List<AttachDto> updateAttachForEntity(Long entityId, AttachType type, List<Long> updateAttachRequests) {
        List<Attaches> listAttaches = attachRepository.findAttachByEntities(List.of(entityId), type);

        List<Long> currentAttachIds = listAttaches.stream().map(Attaches::getId).collect(Collectors.toList());
        List<Long> newAssignAttachIds = new ArrayList<>();

        for (Long id: updateAttachRequests) {
            currentAttachIds.remove(id);
            if (!currentAttachIds.contains(id)) {
                newAssignAttachIds.add(id);
            }
        }

        assignAttachesForEntity(newAssignAttachIds, entityId, type);
        detachAttachForEntity(currentAttachIds, entityId, type);
        listAttaches = attachRepository.findAttachByEntities(List.of(entityId), type);
        return AttachMapper.INSTANCE.toDtoList(listAttaches);
    }
}
