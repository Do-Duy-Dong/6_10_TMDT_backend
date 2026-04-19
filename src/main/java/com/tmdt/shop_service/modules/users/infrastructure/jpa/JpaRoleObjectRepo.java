package com.tmdt.shop_service.modules.users.infrastructure.jpa;

import com.tmdt.shop_service.modules.users.domain.model.RoleObject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleObjectRepo extends JpaRepository<RoleObject, Long> {
}
