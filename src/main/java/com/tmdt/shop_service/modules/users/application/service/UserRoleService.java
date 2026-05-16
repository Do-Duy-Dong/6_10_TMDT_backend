package com.tmdt.shop_service.modules.users.application.service;

import com.tmdt.shop_service.modules.users.domain.model.Role;
import com.tmdt.shop_service.modules.users.domain.model.UserRole;

import java.util.List;

public interface UserRoleService {
    UserRole save(UserRole userRole);

    List<Role> getAllRoleOfUser(Long userId);
}
