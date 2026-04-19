package com.tmdt.shop_service.modules.users.infrastructure.repository;

import com.tmdt.shop_service.modules.users.domain.model.Role;
import com.tmdt.shop_service.modules.users.domain.repo.RoleRepository;
import com.tmdt.shop_service.modules.users.infrastructure.jpa.JpaRoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class RoleRepositoryImpl implements RoleRepository {
    final JpaRoleRepo jpaRoleRepository;


    @Override
    public Optional<Role> getRoleByCode(String code) {
        return jpaRoleRepository.findByCode(code);
    }
}
