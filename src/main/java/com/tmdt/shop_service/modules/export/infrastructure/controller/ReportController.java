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

    @GetMapping
    public Map<Object, Object> report(
            @RequestParam(name = "type") ReportType type,
            @RequestParam(name = "fromDate") LocalDate fromDate,
            @RequestParam(name = "toDate") LocalDate toDate) {
        return reportService.report(type, fromDate, toDate);
    }
}
