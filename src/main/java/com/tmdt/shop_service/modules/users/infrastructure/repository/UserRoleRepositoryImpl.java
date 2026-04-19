package com.tmdt.shop_service.modules.users.infrastructure.repository;

import com.tmdt.shop_service.core.exception.DuplicateResourceException;
import com.tmdt.shop_service.modules.users.domain.model.UserRole;
import com.tmdt.shop_service.modules.users.domain.repo.UserRoleRepository;
import com.tmdt.shop_service.modules.users.infrastructure.jpa.JpaUserRoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRoleRepositoryImpl implements UserRoleRepository {
    final JpaUserRoleRepo jpaUserRoleRepo;

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
}
