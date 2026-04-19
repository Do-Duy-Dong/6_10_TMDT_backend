package com.tmdt.shop_service.modules.users.infrastructure.jpa;

import com.tmdt.shop_service.modules.users.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaRoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByCode(String code);
}
