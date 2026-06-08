package com.tmdt.shop_service.modules.laptop.application.dto;

import com.tmdt.shop_service.modules.attaches.application.dto.AttachDto;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptionLaptopDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long laptopId;
    private AttachDto attach;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}
