package com.tmdt.shop_service.modules.laptop.application.service;

import com.tmdt.shop_service.core.exception.ResourceNotFoundException;
import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.application.mapper.LaptopMapper;
import com.tmdt.shop_service.modules.laptop.application.request.CreateLaptopRequest;
import com.tmdt.shop_service.modules.laptop.application.request.UpdateLaptopRequest;
import com.tmdt.shop_service.modules.laptop.domain.model.Laptop;
import com.tmdt.shop_service.modules.laptop.domain.repo.LaptopRepo;
import com.tmdt.shop_service.utils.Constant;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class LaptopServiceImpl implements LaptopService {
    final LaptopRepo laptopRepo;

    @Override
    public LaptopDto create(@NotNull CreateLaptopRequest request, @NotNull Long userId) {
        Laptop laptop = new Laptop(
                request.getName(),
                request.getDescription(),
                request.getIsActive(),
                userId,
                request.getOriginalPrice()
        );

        laptop = laptopRepo.save(laptop);
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
        laptop = laptopRepo.save(laptop);
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
        return LaptopMapper.INSTANCE.toDto(laptop);
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
}
