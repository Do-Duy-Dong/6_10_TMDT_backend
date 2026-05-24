package com.tmdt.shop_service.modules.categories.infrastructure.repo;

import com.tmdt.shop_service.modules.categories.application.dto.BaseCategoryDto;
import com.tmdt.shop_service.modules.categories.domain.model.BaseCategory;
import com.tmdt.shop_service.modules.categories.domain.repo.BaseCategoryRepo;
import com.tmdt.shop_service.modules.categories.infrastructure.jpa.JpaBaseCategoryRepo;
import com.tmdt.shop_service.utils.StringUtils;
import lombok.RequiredArgsConstructor;
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

@Repository
@RequiredArgsConstructor
public class BaseCategoryRepoImpl implements BaseCategoryRepo {
    private final List<String> sortFields = List.of("name", "code", "create_at", "id", "is_active");
    final JpaBaseCategoryRepo jpaBaseCategoryRepo;
    final NamedParameterJdbcTemplate paramterJdbcTemplate;

    @Override
    public BaseCategory save(BaseCategory baseCategory) {
        return jpaBaseCategoryRepo.save(baseCategory);
    }

    @Override
    public Optional<BaseCategory> findById(Long id) {
        return jpaBaseCategoryRepo.findById(id);
    }

    @Override
    public Optional<BaseCategory> findByCode(String code) {
        return jpaBaseCategoryRepo.findByCode(code);
    }

    @Override
    public void delete(Long id) {
        jpaBaseCategoryRepo.deleteById(id);
    }

    @Override
    public Page<BaseCategoryDto> getList(Pageable pageable, String nameCt, String codeEq, Integer isActive) {
        Map<String, Object> params = new HashMap<>();
        String selectSql = "select * from base_category\n";
        String countSql = "select count(*) from base_category\n";

        String condition = "where 1=1 \n";

        if (isActive != null) {
            condition += "  and is_active = :isActive \n";
            params.put("isActive", isActive);
        }
        if (nameCt != null) {
            condition += "  and lower(name) like :nameCt \n";
            params.put("nameCt", StringUtils.likeLowerContentString(nameCt));
        }
        if (codeEq != null) {
            condition += "  and code = :codeEq \n";
            params.put("codeEq", codeEq);
        }

        countSql += condition;
        selectSql += condition + StringUtils.genDirection(sortFields, pageable, null);

        Long total = paramterJdbcTemplate.queryForObject(countSql, params, Long.class);

        List<BaseCategoryDto> dtoList = paramterJdbcTemplate.query(selectSql, params, (rs, rowNumber) -> {
            Timestamp createAt = rs.getTimestamp("create_at");
            Timestamp updateAt = rs.getTimestamp("update_at");
            BaseCategoryDto dto = BaseCategoryDto.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .code(rs.getString("code"))
                    .isActive(rs.getInt("is_active"))
                    .updateAt(updateAt != null ? updateAt.toLocalDateTime() : null)
                    .createAt(createAt != null ? createAt.toLocalDateTime() : null)
                    .build();
            return dto;
        });

        return new PageImpl<>(dtoList, pageable, total);
    }
}
