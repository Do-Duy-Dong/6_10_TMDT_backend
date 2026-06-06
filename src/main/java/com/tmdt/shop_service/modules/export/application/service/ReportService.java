package com.tmdt.shop_service.modules.export.application.service;

import com.tmdt.shop_service.modules.export.domain.model.ReportType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

public interface ReportService {
    Map<Object, Object> report(ReportType type, LocalDate fromDate, LocalDate toDate);
}
