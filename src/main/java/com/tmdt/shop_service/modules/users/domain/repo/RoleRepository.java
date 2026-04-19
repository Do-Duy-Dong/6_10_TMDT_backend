package com.tmdt.shop_service.modules.users.domain.repo;

import com.tmdt.shop_service.modules.users.domain.model.Role;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> getRoleByCode(String code);
}
