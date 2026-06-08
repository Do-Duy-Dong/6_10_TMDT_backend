package com.tmdt.shop_service.modules.cart.application.service;

import com.tmdt.shop_service.core.exception.ForbiddenException;
import com.tmdt.shop_service.core.exception.ResourceNotFoundException;
import com.tmdt.shop_service.modules.attaches.application.dto.AttachDto;
import com.tmdt.shop_service.modules.attaches.application.service.AttachService;
import com.tmdt.shop_service.modules.attaches.domain.AttachType;
import com.tmdt.shop_service.modules.cart.application.dto.CartDto;
import com.tmdt.shop_service.modules.cart.application.mapper.CartMapper;
import com.tmdt.shop_service.modules.cart.application.request.CartRequest;
import com.tmdt.shop_service.modules.cart.domain.model.Cart;
import com.tmdt.shop_service.modules.cart.domain.repo.CartRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepo;
    private final CartMapper cartMapper;
    private final AttachService attachService;

    @Override
    @Transactional
    public CartDto addToCart(Long ownerId, CartRequest request) {
        Optional<Cart> existingCartOpt = cartRepo.findByOwnerIdAndOptionId(ownerId, request.getOptionId());
        if (existingCartOpt.isPresent()) {
            Cart existingCart = existingCartOpt.get();
            existingCart.setQuantity(existingCart.getQuantity() + request.getQuantity());
            return cartMapper.toDto(cartRepo.save(existingCart));
        }

        Cart newCart = new Cart();
        newCart.setOwnerId(ownerId);
        newCart.setOptionId(request.getOptionId());
        newCart.setQuantity(request.getQuantity());
        return cartMapper.toDto(cartRepo.save(newCart));
    }

    @Override
    @Transactional
    public CartDto updateCartQuantity(Long ownerId, Long cartId, Integer quantity) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cart.getOwnerId().equals(ownerId)) {
            throw new ForbiddenException("You do not have permission to modify this cart");
        }

        if (quantity <= 0) {
            cartRepo.delete(cartId);
            return null;
        }

        cart.setQuantity(quantity);
        return cartMapper.toDto(cartRepo.save(cart));
    }

    @Override
    @Transactional
    public void removeFromCart(Long ownerId, Long cartId) {
        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        if (!cart.getOwnerId().equals(ownerId)) {
            throw new ForbiddenException("You do not have permission to modify this cart");
        }

        cartRepo.delete(cartId);
    }

    @Override
    public List<CartDto> getCartByOwnerId(Long ownerId) {
        Map<Long, CartDto> cartDtoMap = cartRepo.findByOwnerId(ownerId).stream()
                .collect(Collectors.toMap(CartDto::getOptionId, Function.identity()));

        List<Long> optionIds = cartDtoMap.values().stream().map(CartDto::getOptionId).toList();
        List<AttachDto> attachDtos = attachService.getAttachDtoForEntities(optionIds, AttachType.OPTION_LAPTOP);

        for (AttachDto attachDto : attachDtos) {
            CartDto cartDto = cartDtoMap.get(attachDto.moduleId());
            cartDto.setImageKey(attachDto.attachMetadata().getKeyName());
        }

        return cartDtoMap.values().stream().toList();
    }
}
