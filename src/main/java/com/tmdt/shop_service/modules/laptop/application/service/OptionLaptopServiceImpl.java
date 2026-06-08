package com.tmdt.shop_service.modules.laptop.application.service;

import com.tmdt.shop_service.core.exception.ResourceNotFoundException;
import com.tmdt.shop_service.modules.attaches.application.dto.AttachDto;
import com.tmdt.shop_service.modules.attaches.application.service.AttachService;
import com.tmdt.shop_service.modules.attaches.domain.AttachType;
import com.tmdt.shop_service.modules.laptop.application.dto.OptionLaptopDto;
import com.tmdt.shop_service.modules.laptop.application.mapper.OptionLaptopMapper;
import com.tmdt.shop_service.modules.laptop.application.request.CreateLaptopOptionRequest;
import com.tmdt.shop_service.modules.laptop.domain.model.OptionLaptop;
import com.tmdt.shop_service.modules.laptop.domain.repo.OptionLaptopRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OptionLaptopServiceImpl implements OptionLaptopService {
    private final OptionLaptopRepo optionLaptopRepo;
    private final AttachService attachService;

    @Override
    @Transactional
    public OptionLaptopDto save(CreateLaptopOptionRequest request, Long laptopId) {
        OptionLaptop optionLaptop = new OptionLaptop(
                request.getName(),
                request.getPrice(),
                laptopId);
        attachService.assignAttachesForEntity(List.of(request.getAttachId()), optionLaptop.getLaptopId(), AttachType.OPTION_LAPTOP);
        return OptionLaptopMapper.INSTANCE.toDto(optionLaptopRepo.save(optionLaptop));
    }

    @Override
    @Transactional
    public List<OptionLaptopDto> saveAll(List<CreateLaptopOptionRequest> requests, Long laptopId) {
        List<OptionLaptop> entities = requests.stream().map(request -> {
            OptionLaptop optionLaptop = new OptionLaptop(
                            request.getName(),
                            request.getPrice(),
                            laptopId);
            optionLaptopRepo.save(optionLaptop);
            attachService.assignAttachesForEntity(List.of(request.getAttachId()), optionLaptop.getId(), AttachType.OPTION_LAPTOP);
            return optionLaptop;
        }).toList();

        return OptionLaptopMapper.INSTANCE.toDtoList(entities);
    }

    @Override
    public OptionLaptopDto findById(Long id) {
        return optionLaptopRepo.findById(id)
                .map(OptionLaptopMapper.INSTANCE::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("OptionLaptop not found"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        optionLaptopRepo.delete(id);
    }

    @Override
    @Transactional
    public void deleteByLaptopId(Long laptopId) {
        optionLaptopRepo.deleteByLaptopId(laptopId);
    }

    @Override
    public List<OptionLaptopDto> findByLaptopId(Long laptopId) {
        List<OptionLaptopDto> optionLaptopDtos = OptionLaptopMapper.INSTANCE.toDtoList(
                optionLaptopRepo.findByLaptopId(laptopId));
        Map<Long, OptionLaptopDto> optionLaptopDtoMap = optionLaptopDtos.stream()
                .collect(Collectors.toMap(OptionLaptopDto::getId, Function.identity()));

        List<AttachDto> attachDtos = attachService.getAttachDtoForEntities(
                optionLaptopDtos.stream().map(OptionLaptopDto::getId).toList(),
                AttachType.OPTION_LAPTOP);

        for (AttachDto attachDto: attachDtos) {
            OptionLaptopDto optionLaptopDto = optionLaptopDtoMap.get(attachDto.moduleId());
            optionLaptopDto.setAttach(attachDto);
        }

        return optionLaptopDtoMap.values().stream().toList();
    }

    @Override
    public List<OptionLaptopDto> update(Long laptopId, List<CreateLaptopOptionRequest> requests) {
        List<OptionLaptop> optionLaptops = optionLaptopRepo.findByLaptopId(laptopId);
        List<CreateLaptopOptionRequest> listCreateRequest = requests.stream()
                .filter(request -> Objects.isNull(request.getId()))
                .toList();

        List<Long> listUpdateRequestIds = requests.stream()
                .map(CreateLaptopOptionRequest::getId)
                .filter(id -> !Objects.isNull(id))
                .toList();

        List<CreateLaptopOptionRequest> listUpdateRequests = requests.stream()
                .filter(request -> !Objects.isNull(request.getId()))
                .toList();

        List<OptionLaptop> removeList = optionLaptops.stream()
                .filter(optionLaptop -> !listUpdateRequestIds.contains(optionLaptop.getId()))
                        .toList();

        Map<Long, OptionLaptop> updateList = optionLaptops.stream()
                .filter(optionLaptop -> listUpdateRequestIds.contains(optionLaptop.getId()))
                        .collect(Collectors.toMap(OptionLaptop::getId, Function.identity()));

        for(CreateLaptopOptionRequest request: listUpdateRequests) {
            OptionLaptop optionLaptop = updateList.get(request.getId());
            attachService.updateAttachForEntity(optionLaptop.getId(), AttachType.OPTION_LAPTOP, List.of(request.getAttachId()));
            optionLaptop.setPrice(request.getPrice());
            optionLaptop.setName(request.getName());
        }

        optionLaptopRepo.saveAll(updateList.values().stream().toList());
        saveAll(listCreateRequest, laptopId);

        return findByLaptopId(laptopId);
    }
}
