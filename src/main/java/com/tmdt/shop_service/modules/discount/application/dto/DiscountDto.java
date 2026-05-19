package com.tmdt.shop_service.modules.discount.application.dto;

import com.tmdt.shop_service.modules.discount.domain.DiscountType;
import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {
    private Long id;
    private String name;
    private String code;
    private List<Long> userIds;
    private Integer quantity;
    private List<Long> moduleIds;
    private DiscountType type;
    private Integer isActive;
    private LocalDateTime expiryFrom;
    private LocalDateTime expiryTo;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<LaptopDto> laptops;
    private List<UserDto> users;
    private Long value;
}
