package com.tmdt.shop_service.modules.export.infrastructure.repo;

import com.tmdt.shop_service.modules.order.domain.ProcessStatus;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class ReportRepo {
    final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Map<Object, Object> baoCaoDoanhThu(LocalDate fromDate, LocalDate toDate) {
        StringBuilder sql = new StringBuilder("select sum(orders.total) as total_orders,\n" +
                "       date_trunc('month', process.create_at) as time_sell\n" +
                "from orders\n" +
                "join process on orders.id = process.order_id\n" +
                "where process.status = :status\n" +
                "  and orders.create_at >= date_trunc('day', cast(:fromDate as timestamp))\n" +
                "  and orders.create_at < date_trunc('day', cast(:toDate as timestamp))\n" +
                "group by date_trunc('month', process.create_at)\n" +
                "order by time_sell asc\n");
        Map<String, Object> params = new HashedMap<>();
        params.put("status", ProcessStatus.HOAN_THANH.getValue());
        params.put("fromDate", fromDate);
        params.put("toDate", toDate.plusDays(1));

        Map<Object, Object> object = new LinkedHashMap<>();

        namedParameterJdbcTemplate.query(sql.toString(), params, (rs) -> {
            LocalDate date = rs.getDate("time_sell").toLocalDate();
            BigDecimal total = rs.getBigDecimal("total_orders");
            object.put(date, total);
        });

        return object;
    }
}
