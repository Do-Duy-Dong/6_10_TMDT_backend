package com.tmdt.shop_service.modules.laptop.infrastructure.repo;

import com.tmdt.shop_service.modules.discount.domain.DiscountType;
import com.tmdt.shop_service.modules.laptop.application.dto.LaptopDto;
import com.tmdt.shop_service.modules.laptop.application.dto.PublicLaptopDto;
import com.tmdt.shop_service.modules.laptop.domain.model.Laptop;
import com.tmdt.shop_service.modules.laptop.domain.repo.LaptopRepo;
import com.tmdt.shop_service.modules.laptop.infrastructure.jpa.JpaLaptopRepo;
import com.tmdt.shop_service.modules.warehouse.application.dto.WarehouseDto;
import com.tmdt.shop_service.modules.warehouse.domain.StoreModelStatus;
import com.tmdt.shop_service.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

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
        selectSql += condition + StringUtils.genDirection(sortFields, pageable, null);

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

    @Override
    public List<Laptop> findByIds(List<Long> ids) {
        return jpaLaptopRepo.findByIdIn(ids);
    }

    @Override
    public Page<PublicLaptopDto> getPublicLaptopDtoByParams(
            Pageable pageable,
            String nameCt,
            Long brandId,
            Long cpuId,
            Long ramId,
            Long storageId,
            Long priceGe,
            Long priceLe,
            Long userId) {
        Map<String, Object> params = new HashedMap<>();
        StringBuilder sql = new StringBuilder("with base_laptop as (\n" +
                "    select\n" +
                "        lt.id,\n" +
                "        lt.name,\n" +
                "        ol.price as original_price,\n" +
                "        lt.create_at,\n" +
                "        lt.slug,\n" +
                "        lt.ram_id,\n" +
                "        lt.brand_id,\n" +
                "        lt.storage_id,\n" +
                "        lt.cpu_id,\n" +
                "        dc.type,\n" +
                "        dc.value,\n" +
                "        get_price_after_sale(ol.price, dc) as price_after,\n" +
                "        row_number() over (\n" +
                "            partition by lt.id\n" +
                "            order by get_price_after_sale(ol.price, dc) asc\n" +
                "            ) as rn\n" +
                "    from laptop lt\n" +
//                "             join categories brand\n" +
//                "                  on brand.id = lt.brand_id and brand.is_active = 1\n" +
//                "             join categories cpu\n" +
//                "                  on cpu.id = lt.cpu_id and cpu.is_active = 1\n" +
//                "             join categories gpu\n" +
//                "                  on gpu.id = lt.gpu_id and gpu.is_active = 1\n" +
//                "             join categories storage\n" +
//                "                  on storage.id = lt.storage_id and storage.is_active = 1\n" +
//                "             join categories screen\n" +
//                "                  on screen.id = lt.screen_id and screen.is_active = 1\n" +
//                "             join categories ss\n" +
//                "                  on ss.id = lt.screen_size_id and ss.is_active = 1\n" +
//                "             join categories ram\n" +
//                "                  on ram.id = lt.ram_id and ram.is_active = 1\n" +
                "             join option_laptop ol\n" +
                "                  on ol.laptop_id = lt.id\n" +
                "             left join discount dc\n" +
                "                       on dc.module_ids is null\n" +
                "                           or dc.user_ids is null\n" +
                "                           or dc.module_ids @> to_jsonb(lt.id)\n" +
                "                           or dc.user_ids @> to_jsonb(cast(:userId as bigint))\n" +
                "    where 1 = 1\n");

        params.put("userId", userId);
        sql.append("  and dc.quantity > 0\n" +
                "       and (dc.expiry_from is null or dc.expiry_from >= now())\n" +
                "       and (dc.expiry_to is null or dc.expiry_to <= now())\n");
        if (nameCt != null && !nameCt.isEmpty()) {
            sql.append("and lower(lt.name) like :nameCt\n");
            params.put("nameCt", StringUtils.likeLowerContainString(nameCt));
        }
        if (brandId != null) {
            sql.append("and lt.brand_id = :brandId\n");
            params.put("brandId", brandId);
        }
        if (cpuId != null) {
            sql.append("and lt.cpu_id = :cpuId\n");
            params.put("cpuId", cpuId);
        }
        if (storageId != null) {
            sql.append("and lt.storage_id = :storageId\n");
            params.put("storageId", storageId);
        }
        if (ramId != null) {
            sql.append("and lt.ram_id = :ramId\n");
            params.put("ramId", ramId);
        }

        sql.append(")\n");
        sql.append("select bl.id,\n" +
                "       bl.name,\n" +
                "       bl.type,\n" +
                "       bl.value,\n" +
                "       bl.original_price,\n" +
                "       bl.storage_id,\n" +
                "       bl.cpu_id,\n" +
                "       bl.ram_id,\n" +
                "       bl.brand_id,\n" +
                "       bl.create_at,\n" +
                "       bl.slug,\n" +
                "       bl.price_after as price,\n" +
                "       count(bl.id) over() as total_element\n" +
                "from base_laptop bl\n" +
                "where bl.rn = 1\n");

        if (priceLe != null) {
            sql.append("and bl.price_after <= :priceLe\n");
            params.put("priceLe", priceLe);
        }
        if (priceGe != null) {
            sql.append("and bl.price_after >= :priceGe\n");
            params.put("priceGe", priceGe);
        }

        List<String> sortFields = List.of("create_at", "price");
        String pagination = StringUtils.genDirection(sortFields, pageable, "");
        sql.append(pagination);
        AtomicLong totalElements = new AtomicLong();
        DiscountType.DiscountTypeConverter converter = new DiscountType.DiscountTypeConverter();
        List<PublicLaptopDto> publicLaptopDtos = paramterJdbcTemplate.query(sql.toString(), params, (rs, rowNumber) -> {
            totalElements.set(rs.getLong("total_element"));
            PublicLaptopDto dto = PublicLaptopDto.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .cpuId(rs.getLong("cpu_id"))
                    .brandId(rs.getLong("brand_id"))
                    .storageId(rs.getLong("storage_id"))
                    .ramId(rs.getLong("ram_id"))
                    .slug(rs.getString("slug"))
                    .discountType(converter.convertToEntityAttribute(rs.getInt("type")))
                    .discountValue(rs.getString("value"))
                    .originalPrice(rs.getString("original_price"))
                    .price(rs.getString("price"))
                    .build();
            return dto;
        });

        return new PageImpl<>(publicLaptopDtos, pageable, totalElements.get());
    }

    @Override
    public Optional<Laptop> findBySlug(String slug) {
        return jpaLaptopRepo.findLaptopBySlug(slug);
    }

    @Override
    public List<Laptop> findLaptopsByParentIdInOrIdIn(List<Long> parentId, List<Long> idIn) {
        return jpaLaptopRepo.findLaptopsByParentIdInOrIdIn(parentId, idIn);
    }

    @Override
    public List<WarehouseDto> getStoreModelDtoHasProduct(Long laptopId, Long optionId) {
        Map<String, Object> params = new HashedMap<>();
        StringBuilder sql = new StringBuilder("with base_store as (\n" +
                "    select sm.status,\n" +
                "           sm.warehouse_id,\n" +
                "           count(sm.option_id) as total\n" +
                "    from store_model sm\n" +
                "    where sm.laptop_id = :laptopId\n" +
                "      and sm.option_id = :optionId\n" +
                "      and sm.status = :status\n" +
                "    group by sm.status,\n" +
                "             sm.warehouse_id,\n" +
                "             sm.laptop_id,\n" +
                "             sm.option_id\n" +
                "    having count(sm.option_id) > 0\n" +
                ")\n" +
                "select w.id, w.name, w.address, bs.total\n" +
                "from warehouse w\n" +
                "join base_store bs on bs.warehouse_id = w.id\n" +
                "where w.is_active = 1");

        params.put("laptopId", laptopId);
        params.put("optionId", optionId);
        params.put("status", StoreModelStatus.NEW.getValue());

        return paramterJdbcTemplate.query(sql.toString(), params, (rs, rn) -> {
            return WarehouseDto.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .address(rs.getString("address"))
                    .total(rs.getLong("total"))
                    .build();
        });
    }
}
