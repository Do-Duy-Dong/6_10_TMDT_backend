package com.tmdt.shop_service.modules.users.infrastructure.jpa;

import com.tmdt.shop_service.modules.users.domain.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRoleRepo extends JpaRepository<UserRole,Long> {
}
