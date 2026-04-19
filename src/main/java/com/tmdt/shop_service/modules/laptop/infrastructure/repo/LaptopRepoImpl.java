package com.tmdt.shop_service.modules.laptop.infrastructure.repo;

import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.domain.model.Laptop;
import com.tmdt.shop_service.modules.laptop.domain.repo.LaptopRepo;
import com.tmdt.shop_service.modules.laptop.infrastructure.jpa.JpaLaptopRepo;
import com.tmdt.shop_service.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LaptopRepoImpl implements LaptopRepo {
    private final List<String> sortFields = List.of("name", "create_at", "id", "is_active", "original_price");
    final JpaLaptopRepo jpaLaptopRepo;
    final NamedParameterJdbcTemplate paramterJdbcTemplate;

    @Override
    public Laptop save(Laptop laptop) {
        return jpaLaptopRepo.save(laptop);
    }

    @Override
    public Optional<Laptop> findById(Long laptopId) {
        return jpaLaptopRepo.findById(laptopId);
    }

    @Override
    public void delete(Long id) {
        jpaLaptopRepo.deleteById(id);
    }

    @Override
    public Page<LaptopDto> getList(Pageable pageable, String nameCt, Integer isActive, BigDecimal originalPriceGe, BigDecimal originalPriceLe) {
        Map<String, Object> params = new HashMap<>();
        String selectSql = "select * from laptop\n";
        String countSql = "select count(*) from laptop\n";

        String condition = "where 1=1 \n";

        if (isActive != null) {
            condition += "  and is_active = :isActive \n";
            params.put("isActive", isActive);
        }
        if (nameCt != null) {
            condition += "  and lower(name) like :nameCt \n";
            params.put("nameCt", StringUtils.likeLowerContentString(nameCt));
        }
        if (originalPriceGe != null) {
            condition += "  and original_price >= :originalPriceGe \n";
            params.put("originalPriceGe", originalPriceGe);
        }
        if (originalPriceLe != null) {
            condition += "  and original_price <= :originalPriceLe \n";
            params.put("originalPriceLe", originalPriceLe);
        }

        countSql += condition;
        selectSql += condition + StringUtils.genDirection(sortFields, pageable);

        Long total = paramterJdbcTemplate.queryForObject(countSql, params, Long.class);

        List<LaptopDto> dtoList = paramterJdbcTemplate.query(selectSql, params, (rs, rowNumber) -> {
            Timestamp createAt = rs.getTimestamp("create_at");
            Timestamp updateAt = rs.getTimestamp("update_at");
            LaptopDto dto = LaptopDto.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .originalPrice(rs.getBigDecimal("original_price"))
                    .isActive(rs.getInt("is_active"))
                    .createBy(rs.getLong("create_by"))
                    .updateAt(updateAt != null ? updateAt.toLocalDateTime() : null)
                    .createAt(createAt != null ? createAt.toLocalDateTime() : null)
                    .build();
            return dto;
        });

        return new PageImpl<>(dtoList, pageable, total);
    }
}
