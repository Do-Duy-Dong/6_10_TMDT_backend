package com.tmdt.shop_service.modules.discount.infrastructure.repo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmdt.shop_service.modules.discount.application.dto.DiscountDto;
import com.tmdt.shop_service.modules.discount.domain.DiscountType;
import com.tmdt.shop_service.modules.discount.domain.model.Discount;
import com.tmdt.shop_service.modules.discount.domain.repo.DiscountRepo;
import com.tmdt.shop_service.modules.discount.infrastructure.jpa.JpaDiscountRepo;
import com.tmdt.shop_service.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DiscountRepoImpl implements DiscountRepo {
    private final List<String> sortFields = List.of("id", "name", "code", "create_at", "expiry_at");
    final JpaDiscountRepo jpaDiscountRepo;
    final NamedParameterJdbcTemplate paramterJdbcTemplate;
    final ObjectMapper objectMapper;

    @Override
    public Discount save(Discount discount) {
        return jpaDiscountRepo.save(discount);
    }

    @Override
    public Optional<Discount> findById(Long id) {
        return jpaDiscountRepo.findById(id);
    }

    @Override
    public void delete(Long id) {
        jpaDiscountRepo.deleteById(id);
    }

    @Override
    public Page<DiscountDto> getList(Pageable pageable,
                                     String nameCt,
                                     String codeEq,
                                     DiscountType typeEq,
                                     Integer isActive,
                                     LocalDateTime expiryAtGe,
                                     LocalDateTime expiryAtLe,
                                     Long userid,
                                     Long laptopIdEq) {
        Map<String, Object> params = new HashMap<>();
        String selectSql = "select * from discount \n";
        String countSql = "select count(*) from discount \n";

        String condition = "where 1=1 \n";

        if (nameCt != null && !nameCt.trim().isEmpty()) {
            condition += "  and lower(name) like :nameCt \n";
            params.put("nameCt", StringUtils.likeLowerContentString(nameCt));
        }
        if (codeEq != null && !codeEq.trim().isEmpty()) {
            condition += "  and code = :codeEq \n";
            params.put("codeEq", codeEq);
        }
        if (expiryAtGe != null) {
            condition += "  and expiry_from >= :expiryAtGe \n";
            params.put("expiryAtGe" , expiryAtGe);
        }
        if (expiryAtLe != null) {
            condition += "  and expiry_from <= :expiryAtLe \n";
            params.put("expiryAtLe", expiryAtLe);
        }
        if (isActive != null) {
            condition += "  and is_active =:isActive \n";
            params.put("isActive", isActive);
        }
        if (typeEq != null) {
            condition += "  and type=:typeEq \n";
            params.put("typeEq", typeEq.getValue());
        }
        if (userid != null) {
            condition += "  and (user_ids @> to_jsonb(:userId::bigint) or user_ids is null or jsonb_array_length(user_ids) = 0)\n";
            params.put("userId", userid);
        }
        if (laptopIdEq != null) {
            condition += "  and (module_ids @> to_jsonb(:laptopId::bigint) or module_ids is null or jsonb_array_length(module_ids) = 0)\n";
            params.put("laptopId", laptopIdEq);
        }
        if (userid != null || laptopIdEq != null) {
            condition += "  and quantity > 0\n" +
                    "       and is_active = 1\n" +
                    "       and (expiry_from is null or expiry_from >= now())\n" +
                    "       and (expiry_to is null or expiry_to >= now())\n";
        }

        countSql += condition;
        selectSql += condition + genDirection(pageable);

        Long total = paramterJdbcTemplate.queryForObject(countSql, params, Long.class);

        DiscountType.DiscountTypeConverter discountTypeConverter = new DiscountType.DiscountTypeConverter();
        List<DiscountDto> discountDtoList = paramterJdbcTemplate.query(selectSql, params, (rs, rowNumber) -> {
            Timestamp createAt = rs.getTimestamp("create_at");
            Timestamp updateAt = rs.getTimestamp("update_at");
            Timestamp expiryFrom = rs.getTimestamp("expiry_from");
            Timestamp expiryTo = rs.getTimestamp("expiry_to");

            try {
                String userIdsJson = rs.getString("user_ids");
                if (userIdsJson == null) userIdsJson = "[]";
                String moduleIdsJson = rs.getString("module_ids");
                if (moduleIdsJson == null) moduleIdsJson = "[]";
                return DiscountDto.builder()
                        .id(rs.getLong("id"))
                        .name(rs.getString("name"))
                        .code(rs.getString("code"))
                        .quantity(rs.getInt("quantity"))
                        .value(rs.getLong("value"))
                        .isActive(rs.getInt("is_active"))
                        .userIds(objectMapper.readValue(userIdsJson, new TypeReference<List<Long>>() {}))
                        .moduleIds(objectMapper.readValue(moduleIdsJson, new TypeReference<List<Long>>() {}))
                        .type(discountTypeConverter.convertToEntityAttribute(rs.getInt("type")))
                        .updateAt(updateAt != null ? updateAt.toLocalDateTime() : null)
                        .createAt(createAt != null ? createAt.toLocalDateTime() : null)
                        .expiryFrom(expiryFrom != null ? expiryFrom.toLocalDateTime() : null)
                        .expiryTo(expiryTo != null ? expiryTo.toLocalDateTime() : null)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return new PageImpl<>(discountDtoList, pageable, total);
    }

    public String genDirection(Pageable pageable) {
        String paging = "order by ";
        boolean hasOrder = false;
        if (pageable.getSort() != null && pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                if (sortFields.contains(order.getProperty())) {
                    paging += order.getProperty() + " " + order.getDirection() + ", ";
                    hasOrder = true;
                }
            }
        }
        
        if (hasOrder) {
            paging = paging.substring(0, paging.length() - 2) + "\n";
        } else {
            paging = "order by create_at desc \n";
        }
        paging += "limit " + pageable.getPageSize() + " offset " + pageable.getOffset() + "\n";
        return paging;
    }
}
