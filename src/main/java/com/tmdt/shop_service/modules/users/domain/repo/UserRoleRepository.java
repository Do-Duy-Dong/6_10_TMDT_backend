package com.tmdt.shop_service.modules.users.domain.repo;

import com.tmdt.shop_service.modules.users.domain.model.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository {
    UserRole save(UserRole userRole);

    Optional<UserRole> findById(Long id);

    List<UserRole> findByUserId(Long userId);
}
