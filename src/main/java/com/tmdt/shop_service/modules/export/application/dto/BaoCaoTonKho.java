package com.tmdt.shop_service.modules.export.application.dto;

import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class BaoCaoTonKho {
    LocalDate time;
    Long laptopId;
    String laptopName;
    Long tonKhoDauKy;
    Long nhapHang;
    Long xuatHang;
    Long tonKhoCuoiKy;
}
