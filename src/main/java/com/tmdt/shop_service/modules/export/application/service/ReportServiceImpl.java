package com.tmdt.shop_service.modules.export.application.service;

import com.tmdt.shop_service.modules.export.domain.model.ReportType;
import com.tmdt.shop_service.modules.export.infrastructure.repo.ReportRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepo reportRepo;

    @Override
    public Map<Object, Object> report(ReportType type, LocalDate fromDate, LocalDate toDate) {
        return switch (type) {
            case BAO_CAO_DOANH_THU -> reportRepo.baoCaoDoanhThu(fromDate, toDate);
            default -> throw new IllegalArgumentException("Loại báo cáo không hợp lệ");
        };
    }
}
