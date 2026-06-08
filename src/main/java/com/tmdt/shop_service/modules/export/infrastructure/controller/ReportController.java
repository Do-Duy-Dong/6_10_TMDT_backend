package com.tmdt.shop_service.modules.export.infrastructure.controller;

import com.tmdt.shop_service.modules.export.application.service.ReportService;
import com.tmdt.shop_service.modules.export.domain.model.ReportType;
import com.tmdt.shop_service.modules.export.infrastructure.repo.ReportRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/v1/admin/reports")
@RequiredArgsConstructor
public class ReportController {
    final ReportService reportService;
    final ReportRepo reportRepo;

    @GetMapping
    public Object report(
            @RequestParam(name = "type") ReportType type,
            @RequestParam(name = "fromDate") LocalDate fromDate,
            @RequestParam(name = "toDate") LocalDate toDate,
            @RequestParam(name = "warehouseId", required = false) Long warehouseId
    ) {
        return switch (type) {
            case BAO_CAO_DOANH_THU -> reportRepo.baoCaoDoanhThu(fromDate, toDate);
            case BAO_CAO_TON_KHO -> reportRepo.baoCaoTonKho(warehouseId, fromDate, toDate);
            case TON_KHO_HIEN_TAI -> reportRepo.soLuongTonKhoHienTai();
            default -> throw new IllegalArgumentException("Loại báo cáo không hợp lệ");
        };
    }
}
