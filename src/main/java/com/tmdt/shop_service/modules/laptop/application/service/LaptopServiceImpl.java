package com.tmdt.shop_service.modules.laptop.application.service;

import com.tmdt.shop_service.core.exception.ResourceNotFoundException;
import com.tmdt.shop_service.modules.attaches.application.dto.AttachDto;
import com.tmdt.shop_service.modules.attaches.application.service.AttachService;
import com.tmdt.shop_service.modules.attaches.domain.AttachType;
import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.application.dto.OptionLaptopDto;
import com.tmdt.shop_service.modules.laptop.application.dto.PublicLaptopDto;
import com.tmdt.shop_service.modules.laptop.application.mapper.LaptopMapper;
import com.tmdt.shop_service.modules.laptop.application.request.CreateLaptopRequest;
import com.tmdt.shop_service.modules.laptop.application.request.UpdateLaptopRequest;
import com.tmdt.shop_service.modules.laptop.domain.model.Laptop;
import com.tmdt.shop_service.modules.laptop.domain.repo.LaptopRepo;
import com.tmdt.shop_service.modules.warehouse.application.dto.StoreModelDto;
import com.tmdt.shop_service.modules.warehouse.application.dto.WarehouseDto;
import com.tmdt.shop_service.utils.Constant;
import com.tmdt.shop_service.utils.StringUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaptopServiceImpl implements LaptopService {
    final LaptopRepo laptopRepo;
    final AttachService attachService;
    final OptionLaptopService optionLaptopService;

    @Override
    @Transactional
    public LaptopDto create(@NotNull CreateLaptopRequest request, @NotNull Long userId) {
        Laptop laptop = new Laptop(
                request.getName(),
                request.getDescription(),
                request.getIsActive(),
                userId,
                request.getOriginalPrice(),
                request.getParentId(),
                request.getBrandId(),
                request.getRamId(),
                request.getStorageId(),
                request.getScreenSizeId(),
                request.getGpuId(),
                request.getCpuId(),
                request.getScreenId(),
                StringUtils.generatorSlug(request.getSlug()));

        laptop = laptopRepo.save(laptop);

        if(request.getAttachIds() != null && !request.getAttachIds().isEmpty()) {
            attachService.assignAttachesForEntity(request.getAttachIds(), laptop.getId(), AttachType.LAP_TOP);
        }
        optionLaptopService.saveAll(request.getOptions(), laptop.getId());
        return LaptopMapper.INSTANCE.toDto(laptop);
    }

    @Override
    public LaptopDto update(
            @NotNull Long laptopId,
            @NotNull UpdateLaptopRequest request,
            @NotNull Long userId) {
        Laptop laptop = laptopRepo.findById(laptopId).orElseThrow(
                () -> new ResourceNotFoundException("Laptop Not Found"));
        laptop.setName(request.getName());
        laptop.setDescription(request.getDescription());
        laptop.setIsActive(request.getIsActive());
        laptop.setOriginalPrice(request.getOriginalPrice());
        laptop.setBrandId(request.getBrandId());
        laptop.setCpuId(request.getCpuId());
        laptop.setGpuId(request.getGpuId());
        laptop.setSlug(StringUtils.generatorSlug(request.getSlug()));
        laptop.setRamId(request.getRamId());
        laptop.setScreenId(request.getScreenId());
        laptop.setStorageId(request.getStorageId());
        laptop.setScreenSizeId(request.getScreenSizeId());
        laptop = laptopRepo.save(laptop);

        attachService.updateAttachForEntity(laptopId, AttachType.LAP_TOP, request.getAttachIds());
        optionLaptopService.update(laptopId, request.getOptions());

        return LaptopMapper.INSTANCE.toDto(laptop);
    }

    @Override
    public void updateStatus(Long laptopId, Integer status) {
        Laptop laptop = laptopRepo.findById(laptopId).orElseThrow(
                () -> new ResourceNotFoundException("Laptop Not Found"));
        laptop.setIsActive(status);
        laptopRepo.save(laptop);
    }

    @Override
    public void deleteLaptop(@NotNull Long id) {
        Laptop laptop = laptopRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Laptop Not Found"));
        laptopRepo.delete(id);
    }

    @Override
    public LaptopDto getById(Long id) {
        Laptop laptop = laptopRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Laptop Not Found"));
        List<AttachDto> attachDtos = attachService.getAttachDtoForEntity(id, AttachType.LAP_TOP);
        List<OptionLaptopDto> optionLaptopDtos = optionLaptopService.findByLaptopId(laptop.getId());
        var result = LaptopMapper.INSTANCE.toDto(laptop);
        result.setOptions(optionLaptopDtos);
        result.setAttaches(attachDtos);
        return result;
    }

    @Override
    public Page<LaptopDto> getList(Pageable pageable, String nameCt, Integer isActive, BigDecimal originalPriceGe, BigDecimal originalPriceLe) {
        return laptopRepo.getList(pageable, nameCt, isActive, originalPriceGe, originalPriceLe);
    }

    @Override
    public LaptopDto getLaptopByIdHasStatusActive(Long id) {
        Laptop laptop = laptopRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Laptop Not Found"));
        if (!Constant.STATUS.ACTIVE.equals(laptop.getIsActive())) {
            throw new ResourceNotFoundException("Laptop is not active or not found");
        }
        return LaptopMapper.INSTANCE.toDto(laptop);
    }

    @Override
    public List<LaptopDto> getLaptopByIds(List<Long> ids) {
        return LaptopMapper.INSTANCE.toDtoList(laptopRepo.findByIds(ids));
    }

    @Override
    public Page<PublicLaptopDto> getListPublicLaptopDtoByParams(
            Pageable pageable,
            String nameCt,
            Long brandId,
            Long cpuId,
            Long ramId,
            Long storageId,
            Long priceGe,
            Long priceLe,
            Long userId) {
        Page<PublicLaptopDto> pages = laptopRepo.getPublicLaptopDtoByParams(
                pageable, nameCt, brandId, cpuId, ramId, storageId, priceGe, priceLe, userId);

        List<Long> publicLaptopIds = pages.getContent().stream().map(PublicLaptopDto::getId).toList();
        Map<Long, PublicLaptopDto> publicLaptopDtoMap = pages.getContent().stream()
                .collect(Collectors.toMap(PublicLaptopDto::getId, Function.identity()));

        List<AttachDto> attachDtos = attachService.getAttachDtoForEntities(publicLaptopIds, AttachType.LAP_TOP);
        for (AttachDto attachDto: attachDtos) {
            PublicLaptopDto publicLaptopDto = publicLaptopDtoMap.get(attachDto.moduleId());
            if (publicLaptopDto.getAttaches() == null || publicLaptopDto.getAttaches().isEmpty()) {
                publicLaptopDto.setAttaches(new ArrayList<>(attachDtos));
            } else if (publicLaptopDto.getAttaches().getFirst().orderNumber() > attachDto.orderNumber()){
                publicLaptopDto.setAttaches(attachDtos);
            }
        }

        return new PageImpl<>(
                publicLaptopDtoMap.values().stream().toList(),
                pageable,
                pages.getTotalElements());
    }

    @Override
    public LaptopDto getLaptopBySlug(String slug) {
         Laptop laptop = laptopRepo.findBySlug(slug)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Laptop không tồn tại"));
         if (laptop.getIsActive() != 1) {
             throw new IllegalArgumentException("");
         }

         List<Long> parentIds = new ArrayList<>();
         parentIds.add(laptop.getId());
         if (laptop.getParentId() != null ) parentIds.add(laptop.getParentId());
         LaptopDto laptopDto = LaptopMapper.INSTANCE.toDto(laptop);
         Set<LaptopDto> relations = new HashSet<>(findByParentIdInOrIdIn(parentIds));
         laptopDto.setRelations(relations.stream().toList());

         List<AttachDto> attachDtos = attachService.getAttachDtoForEntity(laptop.getId(), AttachType.LAP_TOP);
         laptopDto.setAttaches(attachDtos);

         List<OptionLaptopDto> optionLaptopDtos = optionLaptopService.findByLaptopId(laptopDto.getId());
         laptopDto.setOptions(optionLaptopDtos);

         return laptopDto;
    }

    @Override
    public List<LaptopDto> findByParentIdInOrIdIn(List<Long> parentIdIn) {
        var result = laptopRepo.findLaptopsByParentIdInOrIdIn(parentIdIn, parentIdIn);
        return LaptopMapper.INSTANCE.toDtoList(result);
    }

    @Override
    public List<OptionLaptopDto> getOptionsOfLaptop(Long id) {
        return optionLaptopService.findByLaptopId(id);
    }

    @Override
    public List<WarehouseDto> getStoreModelHasProduct(Long laptopId, Long optionId) {
        return laptopRepo.getStoreModelDtoHasProduct(laptopId, optionId);
    }
}
