package com.tmdt.shop_service.modules.users.application.service;

import com.tmdt.shop_service.core.exception.InternalException;
import com.tmdt.shop_service.modules.users.domain.model.Role;
import com.tmdt.shop_service.modules.users.domain.repo.RoleRepository;
import com.tmdt.shop_service.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    final RoleRepository roleRepository;

    @Override
    public Role getRoleDefault() {
        return roleRepository.getRoleByCode(Constant.ROLE_DEFAULT.CUSTOMER)
                .orElseThrow(() -> new InternalException(""));
    }
}
