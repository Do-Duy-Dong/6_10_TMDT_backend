package com.tmdt.shop_service.modules.export.infrastructure.repo;

import com.tmdt.shop_service.modules.export.application.dto.BaoCaoTonKho;
import com.tmdt.shop_service.modules.order.domain.ProcessStatus;
import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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

    public List<BaoCaoTonKho> baoCaoTonKho(Long warehouseId, LocalDate fromDate, LocalDate toDate) {
        StringBuilder sql = new StringBuilder("with event_series as (\n" +
                "    select st.laptop_id,\n" +
                "           st.create_at as event_time,\n" +
                "           1 as nhap,\n" +
                "           0 as xuat\n" +
                "    from store_model st\n" +
                "    where (cast(:warehouse_id as bigint) is null or st.warehouse_id = :warehouse_id_match)\n" +
                "    union all\n" +
                "    select st.laptop_id,\n" +
                "           st.update_at as event_time,\n" +
                "           0 as nhap,\n" +
                "           1 as xuat\n" +
                "    from store_model st\n" +
                "    where st.status = :sold\n" +
                "      and (cast(:warehouse_id as bigint) is null or st.warehouse_id = :warehouse_id_match)\n" +
                "),\n" +
                "monthly_changes as (\n" +
                "    select es.laptop_id,\n" +
                "           date_trunc('month', es.event_time) as month,\n" +
                "           sum(es.nhap) as nhap_hang,\n" +
                "           sum(es.xuat) as xuat_hang\n" +
                "    from event_series es\n" +
                "    group by es.laptop_id, date_trunc('month', es.event_time)\n" +
                "),\n" +
                "cumulative_changes as (\n" +
                "    select mc.laptop_id,\n" +
                "           lt.name,\n" +
                "           mc.month,\n" +
                "           mc.nhap_hang,\n" +
                "           mc.xuat_hang,\n" +
                "           sum(mc.nhap_hang - mc.xuat_hang) over (\n" +
                "               partition by mc.laptop_id\n" +
                "               order by mc.month asc\n" +
                "           ) as ton_kho_cuoi_ky\n" +
                "    from monthly_changes mc\n" +
                "    join laptop lt on mc.laptop_id = lt.id\n" +
                ")\n" +
                "select laptop_id,\n" +
                "       name,\n" +
                "       month as time_nhap_kho,\n" +
                "       nhap_hang,\n" +
                "       xuat_hang,\n" +
                "       ton_kho_cuoi_ky,\n" +
                "       (ton_kho_cuoi_ky - (nhap_hang - xuat_hang)) as ton_kho_dau_ky\n" +
                "from cumulative_changes\n" +
                "where month >= date_trunc('month', cast(:fromDate as timestamp))\n" +
                "  and month <= date_trunc('month', cast(:toDate as timestamp))");

        Map<String, Object> params = new HashedMap<>();
        params.put("sold", StoreModelStatus.SOLD.getValue());
        params.put("fromDate", fromDate);
        params.put("toDate", toDate);
        params.put("warehouse_id", warehouseId);
        params.put("warehouse_id_match", warehouseId);

        List<BaoCaoTonKho> result = namedParameterJdbcTemplate.query(sql.toString(), params, (rs, rn) -> {
            LocalDate date = rs.getDate("time_nhap_kho").toLocalDate();
            Long tonKhoDauKy = rs.getLong("ton_kho_dau_ky");
            Long nhapHang = rs.getLong("nhap_hang");
            Long xuatHang = rs.getLong("xuat_hang");
            Long tonKhoCuoiKy = rs.getLong("ton_kho_cuoi_ky");

            return BaoCaoTonKho.builder()
                    .laptopId(rs.getLong("laptop_id"))
                    .laptopName(rs.getString("name"))
                    .time(date)
                    .tonKhoDauKy(tonKhoDauKy)
                    .nhapHang(nhapHang)
                    .xuatHang(xuatHang)
                    .tonKhoCuoiKy(tonKhoCuoiKy)
                    .build();
        });
        return result;
    }

    public Map<String, Integer> soLuongTonKhoHienTai() {
        StringBuilder sql = new StringBuilder("select lt.name,\n" +
                "       lt.id,\n" +
                "       count(*) as count_total\n" +
                "from store_model st\n" +
                "         join laptop lt on st.laptop_id = lt.id\n" +
                "where st.status = :new\n" +
                "group by lt.name, lt.id\n" +
                "order by count_total desc");

        Map<String, Object> params = new HashedMap<>();
        params.put("new", StoreModelStatus.NEW.getValue());

        Map<String, Integer> object = new LinkedHashMap<>();

        namedParameterJdbcTemplate.query(sql.toString(), params, (rs) -> {
            String name = rs.getString("name");
            Integer count = rs.getInt("count_total");
            object.put(name, count);
        });

        return object;
    }
}
