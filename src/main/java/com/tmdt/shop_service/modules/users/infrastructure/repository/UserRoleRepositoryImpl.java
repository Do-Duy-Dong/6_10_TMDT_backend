package com.tmdt.shop_service.modules.users.infrastructure.repository;

import com.tmdt.shop_service.core.exception.DuplicateResourceException;
import com.tmdt.shop_service.modules.users.domain.model.Role;
import com.tmdt.shop_service.modules.users.domain.model.UserRole;
import com.tmdt.shop_service.modules.users.domain.repo.UserRoleRepository;
import com.tmdt.shop_service.modules.users.infrastructure.jpa.JpaUserRoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserRoleRepositoryImpl implements UserRoleRepository {
    final JpaUserRoleRepo jpaUserRoleRepo;
    final JdbcTemplate jdbcTemplate;
    final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public UserRole save(UserRole userRole) {
        try {
            return jpaUserRoleRepo.save(userRole);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateResourceException("Role assign to member");
        }
    }

    @Override
    public Optional<UserRole> findById(Long id) {
        return jpaUserRoleRepo.findById(id);
    }

    @Override
    public List<UserRole> findByUserId(Long userId) {
        return findByUserId(userId);
    }

    @Override
    public List<Role> getAllRoleByUserId(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        Map<String, Object> params = new HashMap<>();
        String sql = "select o.*\n" +
                "from users_roles ur\n" +
                "join roles o on o.id = ur.role_id\n" +
                "where o.is_active = 1 and ur.is_active = 1\n" +
                "  and ur.user_id = :userId";
        params.put("userId", userId);
        try {
            return namedParameterJdbcTemplate.query(sql, params, (rs, rowNum) -> {
                Role role = new Role();
                role.setId(rs.getLong("id"));
                role.setName(rs.getString("name"));
                role.setCode(rs.getString("code"));
                role.setIsActive(rs.getInt("is_active"));
                return role;
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
