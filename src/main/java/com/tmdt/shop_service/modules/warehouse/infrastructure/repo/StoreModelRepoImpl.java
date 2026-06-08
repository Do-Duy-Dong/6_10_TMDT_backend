package com.tmdt.shop_service.modules.warehouse.infrastructure.repo;

import com.tmdt.shop_service.modules.warehouse.application.dto.CountStoreModelResponse;
import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import com.tmdt.shop_service.modules.warehouse.domain.model.StoreModel;
import com.tmdt.shop_service.modules.warehouse.domain.repo.StoreModelRepo;
import com.tmdt.shop_service.modules.warehouse.infrastructure.jpa.JpaStoreModelRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
@RequiredArgsConstructor
public class StoreModelRepoImpl implements StoreModelRepo {
    private final JpaStoreModelRepo jpaStoreModelRepo;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public StoreModel save(StoreModel storeModel) {
        return jpaStoreModelRepo.save(storeModel);
    }

    @Override
    public Optional<StoreModel> findById(Long id) {
        return jpaStoreModelRepo.findById(id);
    }

    @Override
    public List<StoreModel> findAll() {
        return jpaStoreModelRepo.findAll();
    }

    @Override
    public List<StoreModel> findByWarehouseId(Long warehouseId) {
        return jpaStoreModelRepo.findByWarehouseId(warehouseId);
    }

    @Override
    public void deleteById(Long id) {
        jpaStoreModelRepo.deleteById(id);
    }

    @Override
    public Optional<StoreModel> findBySerialNumber(String serialNumber) {
        return jpaStoreModelRepo.findBySerialNumber(serialNumber);
    }

    @Override
    public List<StoreModel> findBySerialNumbers(List<String> serialNumbers) {
        return jpaStoreModelRepo.findBySerialNumberIn(serialNumbers);
    }

    @Override
    public Page<StoreModel> findByWarehouseIdAndStatus(Pageable pageable, Long warehouseId, StoreModelStatus status) {
        return jpaStoreModelRepo.findByWarehouseIdAndStatus(pageable, warehouseId, status);
    }

    @Override
    public Page<CountStoreModelResponse> getStoreModelByParams(Pageable pageable, String nameLaptopCt, Long warehouseId, List<StoreModelStatus> statusIn) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder("select\n" +
                "    st.warehouse_id as warehouse_id, \n" +
                "    st.laptop_id as laptop_id, \n" +
                "    l.name as laptop_name, \n" +
                "    w.name as warehouse_name, \n" +
                "    st.status as status, \n" +
                "    count(st.laptop_id) as quantity, \n" +
                "    count(*) over() as total_elements \n");
        sql.append("from store_model st\n" +
                "join laptop l\n" +
                "    on st.laptop_id = l.id\n" +
                "join warehouse w\n" +
                "    on w.id = st.warehouse_id\n");
        sql.append("where 1 = 1 \n");
        if (nameLaptopCt != null && !nameLaptopCt.isEmpty()) {
            sql.append("and lower(l.name) like lower(:nameLaptopCt) \n");
            params.put("nameLaptopCt", "%" + nameLaptopCt.toLowerCase().trim() + "%");
        }
        if (warehouseId != null) {
            sql.append("and st.warehouse_id = :warehouseId\n");
            params.put("warehouseId", warehouseId);
        }

        StoreModelStatus.StoreModelStatusConverter converter = new StoreModelStatus.StoreModelStatusConverter();
//        if (statusIn != null && !statusIn.isEmpty()) {
//            sql.append("and st.status in :status\n");
//            List<Integer> status = statusIn.stream().map(StoreModelStatus::getValue).toList();
//            params.put("status", status);
//        }
        sql.append("group by st.warehouse_id, st.laptop_id, l.name, w.name, st.status\n");
        if (pageable.getSort().isSorted()) {
            List<String> order = pageable.getSort().stream().map(sort -> {
                return switch (sort.getProperty()) {
                    case "laptopName" -> "l.name " + sort.getDirection().name();
                    case "warehouseName" -> "w.name " + sort.getDirection().name();
                    case "quantity" -> "quantity " + sort.getDirection().name();
                    default -> null;
                };
            })
                    .filter(Objects::nonNull)
                    .toList();
            if (!order.isEmpty()) {
                sql.append("order by ");
                sql.append(String.join(", ", order));
                sql.append("\n");
            }
        }
        if (pageable.getPageSize() > 0 && pageable.getPageNumber() >= 0) {
            sql.append("limit :limit offset :offset\n");
            params.put("offset", pageable.getOffset());
            params.put("limit", pageable.getPageSize());
        }

        log.info(sql.toString());

        AtomicLong totalElements = new AtomicLong();
        List<CountStoreModelResponse> countStoreModelResponses = namedParameterJdbcTemplate.query(sql.toString(), params, (result, rowNumber) -> {
            totalElements.set(result.getLong("total_elements"));
            return CountStoreModelResponse.builder()
                    .laptopId(result.getLong("laptop_id"))
                    .warehouseId(result.getLong("warehouse_id"))
                    .laptopName(result.getString("laptop_name"))
                    .warehouseName(result.getString("warehouse_name"))
                    .quantity(result.getLong("quantity"))
                    .status(converter.convertToEntityAttribute(result.getInt("status")))
                    .build();
        });

        return new PageImpl<>(
                countStoreModelResponses,
                pageable,
                totalElements.get());
    }

    @Override
    public List<StoreModel> saveAll(List<StoreModel> storeModels) {
        return jpaStoreModelRepo.saveAll(storeModels);
    }

    @Override
    public List<StoreModel> getListStoreModelByParams(
            Long optionId,
            int number,
            StoreModelStatus status) {
        return jpaStoreModelRepo.getListStoreModelByParams(optionId, number, status);
    }

    @Override
    public void updateStatusByIds(List<Long> storeModelIds, StoreModelStatus status) {
        jpaStoreModelRepo.updateStatusByIds(storeModelIds, status);
    }

    @Override
    public List<StoreModel> findByListId(List<Long> ids) {
        return jpaStoreModelRepo.findAllByIdIn(ids);
    }
}
