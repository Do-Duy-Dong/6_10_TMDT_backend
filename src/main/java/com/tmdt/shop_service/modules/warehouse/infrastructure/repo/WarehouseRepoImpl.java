package com.tmdt.shop_service.modules.warehouse.infrastructure.repo;

import com.tmdt.shop_service.modules.warehouse.application.dto.WarehouseDto;
import com.tmdt.shop_service.modules.warehouse.domain.model.Warehouse;
import com.tmdt.shop_service.modules.warehouse.domain.repo.WarehouseRepo;
import com.tmdt.shop_service.modules.warehouse.infrastructure.jpa.JpaWarehouseRepo;
import com.tmdt.shop_service.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Component
@RequiredArgsConstructor
public class WarehouseRepoImpl implements WarehouseRepo {
    private final JpaWarehouseRepo jpaWarehouseRepo;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private List<String> sortFields = List.of("id", "name", "create_at", "update_at");

    @Override
    public Warehouse save(Warehouse warehouse) {
        return jpaWarehouseRepo.save(warehouse);
    }

    @Override
    public Optional<Warehouse> findById(Long id) {
        return jpaWarehouseRepo.findById(id);
    }

    @Override
    public List<Warehouse> findAll() {
        return jpaWarehouseRepo.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jpaWarehouseRepo.deleteById(id);
    }

    @Override
    public Page<WarehouseDto> getAllWarehousesByParams(Pageable pageable, String nameCt, Integer isActive) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select *, count(*) over() as total_element from warehouse\n");
        sql.append("where 1 = 1 \n");
        if  (nameCt != null && !nameCt.isEmpty()) {
            sql.append("and lower(warehouse.name) like :nameCt \n");
            params.put("nameCt", "%" + nameCt.trim().toLowerCase() + "%");
        }

        if(isActive != null) {
            sql.append("and warehouse.is_active = :isActive \n");
            params.put("isActive", isActive);
        }

        String pagination = StringUtils.genDirection(sortFields, pageable, "warehouse");
        sql.append(pagination);

        AtomicLong totalElements = new AtomicLong();
        List<WarehouseDto> warehouseDtos = namedParameterJdbcTemplate.query(sql.toString(), params, (result, rowNumber) -> {
            totalElements.set(result.getLong("total_element"));
            Timestamp createAt = result.getTimestamp("create_at");
            Timestamp updateAt = result.getTimestamp("update_at");
            return WarehouseDto.builder()
                    .id(result.getLong("id"))
                    .name(result.getString("name"))
                    .address(result.getString("address"))
                    .isActive(result.getInt("is_active"))
                    .createAt(createAt != null ? createAt.toLocalDateTime() : null)
                    .updateAt(updateAt != null ? updateAt.toLocalDateTime() : null)
                    .build();
        });

        return new PageImpl<>(warehouseDtos, pageable, totalElements.get());
    }
}
