package com.tmdt.shop_service.modules.users.application.service;

import com.tmdt.shop_service.modules.users.domain.model.Role;
import com.tmdt.shop_service.modules.users.domain.model.UserRole;
import com.tmdt.shop_service.modules.users.domain.repo.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    final UserRoleRepository userRoleRepository;

    @Override
    public UserRole save(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public List<Role> getAllRoleOfUser(Long userId) {
        return userRoleRepository.getAllRoleByUserId(userId);
    }
}
