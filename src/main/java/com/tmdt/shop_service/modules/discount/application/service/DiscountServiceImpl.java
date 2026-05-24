package com.tmdt.shop_service.modules.discount.application.service;

import com.tmdt.shop_service.core.exception.DuplicateResourceException;
import com.tmdt.shop_service.core.exception.ResourceNotFoundException;
import com.tmdt.shop_service.modules.discount.application.dto.DiscountDto;
import com.tmdt.shop_service.modules.discount.application.mapper.DiscountMapper;
import com.tmdt.shop_service.modules.discount.application.request.CreateDiscountRequest;
import com.tmdt.shop_service.modules.discount.application.request.UpdateDiscountRequest;
import com.tmdt.shop_service.modules.discount.domain.DiscountType;
import com.tmdt.shop_service.modules.discount.domain.model.Discount;
import com.tmdt.shop_service.modules.discount.domain.repo.DiscountRepo;
import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.application.service.LaptopService;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import com.tmdt.shop_service.modules.users.application.service.UserService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {
    final DiscountRepo discountRepo;
    final LaptopService laptopService;
    final UserService userService;

    @Override
    public DiscountDto create(@NotNull CreateDiscountRequest request, @NotNull Long userId) {
        if (request.getExpiryTo() != null && request.getExpiryTo().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Hạn sử dụng không thể trước hiện tại");
        }

        if(request.getExpiryFrom() != null
                && request.getExpiryTo() != null
                && request.getExpiryTo().isBefore(request.getExpiryFrom())) {
            throw new IllegalArgumentException("Thời gian kết thúc không thể sau thời gian bắt đầu");
        }
        Discount discount = new Discount(
                request.getName(),
                request.getCode(),
                request.getUserIds(),
                request.getQuantity(),
                request.getModuleIds(),
                request.getType(),
                request.getExpiryFrom(),
                request.getExpiryTo(),
                request.getIsActive(),
                request.getValue());

        try {
            discount = discountRepo.save(discount);
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage().contains("unique")) {
                throw new DuplicateResourceException("");
            }
            throw new RuntimeException(e);
        }

        return DiscountMapper.INSTANCE.toDto(discount);
    }

    @Override
    public DiscountDto update(
            @NotNull Long id,
            @NotNull UpdateDiscountRequest request,
            @NotNull Long userId) {
        Discount discount = discountRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Mã giảm giá không tồn tại"));
        if (request.getExpiryTo() != null && request.getExpiryTo().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Hạn sử dụng không thể trước hiện tại");
        }

        if(request.getExpiryFrom() != null
                && request.getExpiryTo() != null
                && request.getExpiryTo().isBefore(request.getExpiryFrom())) {
            throw new IllegalArgumentException("Thời gian kết thúc không thể sau thời gian bắt đầu");
        }

        discount.setName(request.getName());
        discount.setCode(request.getCode());
        discount.setUserIds(request.getUserIds());
        discount.setQuantity(request.getQuantity());
        discount.setModuleIds(request.getModuleIds());
        discount.setType(request.getType());
        discount.setExpiryFrom(request.getExpiryFrom());
        discount.setExpiryTo(request.getExpiryTo());
        discount.setIsActive(request.getIsActive());
        discount.setValue(request.getValue());
        discount  = discountRepo.save(discount);
        return DiscountMapper.INSTANCE.toDto(discount);
    }

    @Override
    public void delete(@NotNull Long id) {
        Discount discount = discountRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Discount Not Found"));

        // TODO: không cho xóa nếu đã có lượt sử dụng
        discountRepo.delete(id);
    }

    @Override
    public DiscountDto getById(Long id) {
        Discount discount = discountRepo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Discount Not Found"));
        DiscountDto discountDto = DiscountMapper.INSTANCE.toDto(discount);
        List<LaptopDto> laptopDtos = laptopService.getLaptopByIds(discountDto.getModuleIds());
        List<UserDto> userDtos = userService.findByIdIn(discountDto.getUserIds());
        discountDto.setLaptops(laptopDtos);
        discountDto.setUsers(userDtos);
        return discountDto;
    }

    @Override
    public Page<DiscountDto> getList(
            Pageable pageable,
            String nameCt,
            String codeEq,
            DiscountType typeEq,
            Integer isActive,
            LocalDateTime expiryAtGe,
            LocalDateTime expiryAtLe,
            Long userId,
            Long laptopId) {
        return discountRepo.getList(pageable, nameCt, codeEq, typeEq, isActive, expiryAtGe, expiryAtLe, userId, laptopId);
    }
}
