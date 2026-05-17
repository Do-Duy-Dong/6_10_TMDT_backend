package com.tmdt.shop_service.modules.categories.infrastructure.repository;

import com.tmdt.shop_service.modules.categories.application.dto.CategoryDto;
import com.tmdt.shop_service.modules.categories.domain.model.Category;
import com.tmdt.shop_service.modules.categories.domain.repo.CategoryRepository;
import com.tmdt.shop_service.modules.categories.infrastructure.jpa.JpaCategoryRepository;
import com.tmdt.shop_service.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository {
    private final List<String> sortFields = List.of("name", "code", "create_at", "id", "is_active", "base_code");
    final JpaCategoryRepository jpaCategoryRepository;
    final NamedParameterJdbcTemplate paramterJdbcTemplate;

    @Override
    public Category save(Category category) {
        return jpaCategoryRepository.save(category);
    }

    @Override
    public Optional<Category> findById(Long id) {
        return jpaCategoryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        jpaCategoryRepository.deleteById(id);
    }

    @Override
    public Page<CategoryDto> getList(
            Pageable pageable,
            String nameCt,
            String codeEq,
            String baseCodeEq,
            Integer isActive) {
        Map<String, Object> params = new HashMap<>();
        String alias = "c";
        String selectSql = "select c.*, bc.id as bc_id, bc.code as bc_code, bc.name as bc_name from categories c \n";
        selectSql += "join base_category bc on c.base_code = bc.code \n";
        
        String countSql = "select count(*) from categories c \n";
        countSql += "join base_category bc on c.base_code = bc.code \n";

        String condition = "where 1=1 and bc.is_active = 1 \n";

        if (isActive != null) {
            condition += "  and c.is_active = :isActive \n";
            params.put("isActive", isActive);
        }
        if (nameCt != null) {
            condition += "  and lower(c.name) like :nameCt \n";
            params.put("nameCt", StringUtils.likeLowerContentString(nameCt));
        }
        if (codeEq != null) {
            condition += "  and c.code = :codeEq \n";
            params.put("codeEq", codeEq);
        }
        if (baseCodeEq != null) {
            condition += "  and c.base_code = :baseCodeEq \n";
            params.put("baseCodeEq", baseCodeEq);
        }

        countSql += condition;
        selectSql += condition + StringUtils.genDirection(sortFields, pageable, alias);
        log.info("finalSql " + selectSql);

        Long total = paramterJdbcTemplate.queryForObject(countSql, params, Long.class);

        List<CategoryDto> dtoList = paramterJdbcTemplate.query(selectSql, params, (rs, rowNumber) -> {
            Timestamp createAt = rs.getTimestamp("create_at");
            Timestamp updateAt = rs.getTimestamp("update_at");
            CategoryDto dto = CategoryDto.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .code(rs.getString("code"))
                    .isActive(rs.getInt("is_active"))
                    .baseCodeId(rs.getLong("bc_id"))
                    .baseCode(rs.getString("bc_code"))
                    .baseCodeName(rs.getString("bc_name"))
                    .updateAt(updateAt != null ? updateAt.toLocalDateTime() : null)
                    .createAt(createAt != null ? createAt.toLocalDateTime() : null)
                    .build();
            return dto;
        });

        return new PageImpl<>(dtoList, pageable, total);
    }
}
