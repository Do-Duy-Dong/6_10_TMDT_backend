package com.tmdt.shop_service.modules.order.application.service;

import com.tmdt.shop_service.core.exception.ForbiddenException;
import com.tmdt.shop_service.core.exception.ResourceNotFoundException;
import com.tmdt.shop_service.modules.attaches.application.dto.AttachDto;
import com.tmdt.shop_service.modules.attaches.application.service.AttachService;
import com.tmdt.shop_service.modules.attaches.domain.AttachType;
import com.tmdt.shop_service.modules.cart.domain.model.Cart;
import com.tmdt.shop_service.modules.cart.domain.repo.CartRepo;
import com.tmdt.shop_service.modules.discount.application.mapper.DiscountMapper;
import com.tmdt.shop_service.modules.discount.domain.DiscountType;
import com.tmdt.shop_service.modules.discount.domain.model.Discount;
import com.tmdt.shop_service.modules.discount.domain.repo.DiscountRepo;
import com.tmdt.shop_service.modules.laptop.domain.model.Laptop;
import com.tmdt.shop_service.modules.laptop.domain.model.OptionLaptop;
import com.tmdt.shop_service.modules.laptop.domain.repo.OptionLaptopRepo;
import com.tmdt.shop_service.modules.order.application.dto.OrderDetailDto;
import com.tmdt.shop_service.modules.order.application.dto.OrderDto;
import com.tmdt.shop_service.modules.order.application.mapper.OrderDetailMapper;
import com.tmdt.shop_service.modules.order.application.mapper.OrderMapper;
import com.tmdt.shop_service.modules.order.application.request.OrderRequest;
import com.tmdt.shop_service.modules.order.domain.PaymentStatus;
import com.tmdt.shop_service.modules.order.domain.PaymentType;
import com.tmdt.shop_service.modules.order.domain.ProcessStatus;
import com.tmdt.shop_service.modules.order.domain.ShipmentType;
import com.tmdt.shop_service.modules.order.domain.model.Order;
import com.tmdt.shop_service.modules.order.domain.model.OrderDetail;
import com.tmdt.shop_service.modules.order.domain.model.PaymentDetail;
import com.tmdt.shop_service.modules.order.domain.model.Process;
import com.tmdt.shop_service.modules.order.domain.repo.OrderDetailRepo;
import com.tmdt.shop_service.modules.order.domain.repo.OrderRepo;
import com.tmdt.shop_service.modules.order.domain.repo.PaymentDetailRepo;
import com.tmdt.shop_service.modules.order.domain.repo.ProcessRepo;
import com.tmdt.shop_service.modules.warehouse.application.service.StoreModelService;
import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import com.tmdt.shop_service.modules.warehouse.domain.model.StoreModel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepo orderRepo;
    private final OrderDetailRepo orderDetailRepo;
    private final PaymentDetailRepo paymentDetailRepo;
    private final ProcessRepo processRepo;
    private final CartRepo cartRepo;
    private final OptionLaptopRepo optionLaptopRepo;
    private final DiscountRepo discountRepo;
    private final AttachService attachService;
    private final StoreModelService storeModelService;

    @Override
    @Transactional
    public OrderDto placeOrder(Long userId, OrderRequest request) {
        // 1. Fetch and validate cart items
        List<Cart> carts = new ArrayList<>();
        for (Long cartId : request.getCartIds()) {
            Cart cart = cartRepo.findById(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giỏ hàng với ID: " + cartId));
            if (!Objects.equals(cart.getOwnerId(), userId)) {
                throw new ForbiddenException("Bạn không có quyền đặt hàng từ giỏ hàng này");
            }
            carts.add(cart);
        }

        if (carts.isEmpty()) {
            throw new IllegalArgumentException("Danh sách sản phẩm thanh toán trống");
        }

        // 2. Calculate item total
        BigDecimal itemTotal = BigDecimal.ZERO;
        for (Cart cart : carts) {
            OptionLaptop option = optionLaptopRepo.findById(cart.getOptionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy cấu hình sản phẩm với ID: " + cart.getOptionId()));
            BigDecimal subtotal = option.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity()));
            itemTotal = itemTotal.add(subtotal);
        }

        // 3. Apply discount if discountId is provided
        BigDecimal finalTotal = itemTotal;
        if (request.getDiscountId() != null) {
            Discount discount = discountRepo.findById(request.getDiscountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy mã giảm giá"));

            int excuteDiscount = discountRepo.minus(request.getDiscountId());
            if (excuteDiscount == 0) {
                throw new IllegalArgumentException("Mã giảm giá này đã hết lượt sử dụng");
            }
            // Validate discount
            boolean isApplicable = false;
            if (discount.getModuleIds() == null || discount.getModuleIds().isEmpty()) {
                isApplicable = discount.hasUse(userId, null);
            } else {
                for (Cart cart : carts) {
                    OptionLaptop option = optionLaptopRepo.findById(cart.getOptionId()).orElse(null);
                    if (option != null && discount.getModuleIds().contains(option.getLaptopId())) {
                        if (discount.hasUse(userId, option.getLaptopId())) {
                            isApplicable = true;
                            break;
                        }
                    }
                }
            }

            if (!isApplicable) {
                throw new IllegalArgumentException("Mã giảm giá không hợp lệ hoặc đã hết lượt dùng");
            }

            BigDecimal discountAmount = BigDecimal.ZERO;
            if (discount.getType() == DiscountType.PERCENT) {
                discountAmount = itemTotal.multiply(BigDecimal.valueOf(discount.getValue())).divide(BigDecimal.valueOf(100));
            } else if (discount.getType() == DiscountType.FIXED) {
                discountAmount = BigDecimal.valueOf(discount.getValue());
            }

            finalTotal = itemTotal.subtract(discountAmount);
            if (finalTotal.compareTo(BigDecimal.ZERO) < 0) {
                finalTotal = BigDecimal.ZERO;
            }

            // Deduct discount quantity
            discount.setQuantity(discount.getQuantity() - 1);
            discountRepo.save(discount);
        }

        // 4. Create Order
        Order order = new Order();
        order.setOwnerId(userId);
        order.setDiscountId(request.getDiscountId());
        order.setFullName(request.getFullName());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setEmail(request.getEmail());
        order.setShipmentType(request.getShipmentType());
        order.setPaymentType(request.getPaymentType());
        order.setDistrict(request.getDistrict());
        order.setProvince(request.getProvince());
        order.setCommune(request.getCommune());
        order.setAddressDetail(request.getAddressDetail());
        order.setTotal(finalTotal);
        order = orderRepo.save(order);

        // 5. Create Order Details & delete Carts
        List<OrderDetailDto> orderDetailDtos = new ArrayList<>();
        for (Cart cart : carts) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(order.getId());
            orderDetail.setOptionId(cart.getOptionId());
            orderDetail.setQuantity(cart.getQuantity());
            orderDetail = orderDetailRepo.save(orderDetail);

            List<Long> storeModelIds = storeModelService.getListStoreModelByParams(
                    cart.getOptionId(),
                    cart.getQuantity(),
                    StoreModelStatus.NEW)
                            .stream().map(StoreModel::getId).toList();

            if (storeModelIds.size() != cart.getQuantity()) {
                throw new IllegalArgumentException("Số lượng hàng không đủ cho đơn hàng này!");
            }
            orderDetail.setStoreModelIds(storeModelIds);

            storeModelService.updateStatus(storeModelIds, StoreModelStatus.ORDERED);
            orderDetailDtos.add(OrderDetailMapper.INSTANCE.toDto(orderDetail));

            // Remove item from cart
            cartRepo.delete(cart.getId());
        }

        // 6. Create process with status = NEW (MOI)
        Process process = new Process();
        process.setOrderId(order.getId());
        process.setStatus(ProcessStatus.MOI);
        process.setUpdateBy(userId);
        processRepo.save(process);

        // 7. Create PaymentDetail
        String paymentUrl = null;
        PaymentDetail paymentDetail = new PaymentDetail();
        paymentDetail.setOrderId(order.getId());
        if (request.getPaymentType() == PaymentType.COD) {
            paymentDetail.setStatus(PaymentStatus.COD);
            paymentDetailRepo.save(paymentDetail);
        } else if (request.getPaymentType() == PaymentType.QR) {
            paymentDetail.setStatus(PaymentStatus.NEW);
            paymentDetailRepo.save(paymentDetail);
        }

        // 8. Construct response DTO
        OrderDto orderDto = OrderMapper.INSTANCE.toDto(order);
        orderDto.setOrderDetails(orderDetailDtos);

        return orderDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getMyOrders(Long userId) {
        List<Order> orders = orderRepo.findByOwnerId(userId);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            OrderDto dto = OrderMapper.INSTANCE.toDto(order);
            List<OrderDetail> details = orderDetailRepo.findByOrderId(order.getId());

            Process process = processRepo.findLastProcess(order.getId()).orElse(new Process());
            dto.setStatus(process.getStatus());

            PaymentDetail paymentDetail = paymentDetailRepo.findByOrderId(dto.getId()).orElse(new PaymentDetail());
            dto.setPaymentStatus(paymentDetail.getStatus());

            List<Long> optrionIds = details.stream().map(OrderDetail::getOptionId).toList();
            List<AttachDto> attachDtos = attachService.getAttachDtoForEntities(optrionIds, AttachType.OPTION_LAPTOP);
            Map<Long, OrderDetailDto> orderDetailDtoMap = OrderDetailMapper.INSTANCE.toDtoList(details)
                    .stream().collect(Collectors.toMap(OrderDetailDto::getOptionId, Function.identity()));
            Map<Long, OptionLaptop> optionLaptops = optionLaptopRepo.findByIdIn(optrionIds)
                    .stream().collect(Collectors.toMap(OptionLaptop::getId, Function.identity()));
            Map<Long, Laptop> laptopMap = optionLaptopRepo.findLaptopByOptionIdIn(optrionIds)
                    .stream().collect(Collectors.toMap(Laptop::getId, Function.identity()));
            for (AttachDto attachDto : attachDtos) {
                OrderDetailDto orderDetailDto = orderDetailDtoMap.get(attachDto.moduleId());
                OptionLaptop optionLaptop = optionLaptops.get(attachDto.moduleId());
                Laptop laptop = laptopMap.get(optionLaptop.getLaptopId());
                orderDetailDto.setImageKey(attachDto.attachMetadata().getKeyName());
                orderDetailDto.setName(optionLaptop.getName());
                orderDetailDto.setLaptopName(laptop.getName());
                orderDetailDto.setLaptopSlug(laptop.getSlug());
            }
            dto.setOrderDetails(orderDetailDtoMap.values().stream().toList());
            orderDtos.add(dto);
        }
        return orderDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDto getOrderDetails(Long userId, Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với ID: " + orderId));
        if (!Objects.equals(order.getOwnerId(), userId)) {
            throw new ForbiddenException("Bạn không có quyền xem đơn hàng này");
        }

        return getOrderDetailByOrderId(orderId);
    }

    @Override
    public OrderDto getOrderDetails(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với ID: " + orderId));

        return OrderMapper.INSTANCE.toDto(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDto> getAllOrders() {
        List<Order> orders = orderRepo.findAll();
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            OrderDto dto = OrderMapper.INSTANCE.toDto(order);
            List<OrderDetail> details = orderDetailRepo.findByOrderId(order.getId());
            dto.setOrderDetails(OrderDetailMapper.INSTANCE.toDtoList(details));
            orderDtos.add(dto);
        }
        return orderDtos;
    }

    @Override
    @Transactional
    public OrderDto updateProcessStatus(Long adminId, Long orderId, ProcessStatus status) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với ID: " + orderId));

        Process lastProcess = processRepo.findLastProcess(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Có lỗi xảy ra trong hệ thống"));

        if (Objects.equals(lastProcess.getStatus(), ProcessStatus.HOAN_THANH)) {
            throw new IllegalArgumentException("Đơn hàng này đã hoàn thành không thể đổi trạng thái");
        }
        // Add process status tracking record
        Process process = new Process();
        process.setOrderId(orderId);
        process.setStatus(status);
        process.setUpdateBy(adminId);
        processRepo.save(process);

        // Update payment status to SUCCESS if order is completed
        if (status == ProcessStatus.HOAN_THANH) {
            paymentDetailRepo.findByOrderId(orderId).ifPresent(payment -> {
                payment.setStatus(PaymentStatus.SUCCESS);
                paymentDetailRepo.save(payment);
            });
        }

        if (Objects.equals(status, ProcessStatus.HUY)) {
            List<OrderDetail> orderDetails = orderDetailRepo.findByOrderId(orderId);
            List<Long> storeModelIds = new ArrayList<>();
            for (OrderDetail detail: orderDetails) {
                storeModelIds.addAll(detail.getStoreModelIds());
            }
            storeModelService.updateStatus(storeModelIds, StoreModelStatus.NEW);
        }

        OrderDto dto = OrderMapper.INSTANCE.toDto(order);
        List<OrderDetail> details = orderDetailRepo.findByOrderId(orderId);
        dto.setOrderDetails(OrderDetailMapper.INSTANCE.toDtoList(details));
        return dto;
    }

    @Override
    public Page<OrderDto> getOrderByParams(Pageable pageable, List<PaymentStatus> statusIn, List<ProcessStatus> processStatusIn, ShipmentType shipmentTypeEq, String emailEq) {
        return orderRepo.getOrderByParams(pageable, statusIn, processStatusIn, shipmentTypeEq, emailEq);
    }

    @Override
    public OrderDto getOrderDetailByOrderId(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với ID: " + orderId));

        OrderDto dto = OrderMapper.INSTANCE.toDto(order);
        List<OrderDetail> details = orderDetailRepo.findByOrderId(orderId);

        PaymentDetail paymentDetail = paymentDetailRepo.findByOrderId(orderId).orElse(new PaymentDetail());
        dto.setPaymentStatus(paymentDetail.getStatus());

        Process process = processRepo.findLastProcess(order.getId()).orElse(new Process());
        dto.setStatus(process.getStatus());

        if (order.getDiscountId() != null) {
            Discount discount = discountRepo.findById(order.getDiscountId()).orElse(null);
            dto.setDiscountDto(DiscountMapper.INSTANCE.toDto(discount));
        }
        List<Long> optrionIds = details.stream().map(OrderDetail::getOptionId).toList();
        List<AttachDto> attachDtos = attachService.getAttachDtoForEntities(optrionIds, AttachType.OPTION_LAPTOP);
        Map<Long, OrderDetailDto> orderDetailDtoMap = OrderDetailMapper.INSTANCE.toDtoList(details)
                .stream().collect(Collectors.toMap(OrderDetailDto::getOptionId, Function.identity()));
        Map<Long, OptionLaptop> optionLaptops = optionLaptopRepo.findByIdIn(optrionIds)
                .stream().collect(Collectors.toMap(OptionLaptop::getId, Function.identity()));
        Map<Long, Laptop> laptopMap = optionLaptopRepo.findLaptopByOptionIdIn(optrionIds)
                .stream().collect(Collectors.toMap(Laptop::getId, Function.identity()));

        for (OrderDetailDto orderDetailDto: orderDetailDtoMap.values()) {
            List<String> serialNumbers = storeModelService.findByListId(orderDetailDto.getStoreModelIds())
                    .stream().map(StoreModel::getSerialNumber).toList();
            orderDetailDto.setSerialNumbers(serialNumbers);
        }

        for (AttachDto attachDto : attachDtos) {
            OrderDetailDto orderDetailDto = orderDetailDtoMap.get(attachDto.moduleId());
            OptionLaptop optionLaptop = optionLaptops.get(attachDto.moduleId());
            Laptop laptop = laptopMap.get(optionLaptop.getLaptopId());
            orderDetailDto.setImageKey(attachDto.attachMetadata().getKeyName());
            orderDetailDto.setName(optionLaptop.getName());
            orderDetailDto.setLaptopName(laptop.getName());
            orderDetailDto.setLaptopSlug(laptop.getSlug());
            orderDetailDto.setPrice(optionLaptop.getPrice());
        }
        dto.setOrderDetails(orderDetailDtoMap.values().stream().toList());

        return dto;
    }
}
