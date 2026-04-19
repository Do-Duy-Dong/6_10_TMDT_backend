package com.tmdt.shop_service.modules.auth.application.service;

import com.tmdt.shop_service.modules.auth.application.dto.SignUpDto;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import com.tmdt.shop_service.modules.users.application.mapper.UserMapper;
import com.tmdt.shop_service.modules.users.application.service.RoleService;
import com.tmdt.shop_service.modules.users.application.service.UserRoleService;
import com.tmdt.shop_service.modules.users.application.service.UserService;
import com.tmdt.shop_service.modules.users.domain.model.Role;
import com.tmdt.shop_service.modules.users.domain.model.UserRole;
import com.tmdt.shop_service.modules.users.domain.model.Users;
import com.tmdt.shop_service.utils.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    final PasswordEncoder passwordEncoder;
    final UserService userService;
    final RoleService roleService;
    final UserRoleService userRoleService;

    @Override
    public UserDto signUp(SignUpDto dto) {
        Users users = new Users();
        users.setEmail(dto.getEmail());
        users.setFullName(dto.getFullName());
        users.setGender(dto.getGender());
        users.setIsActive(Constant.STATUS.ACTIVE);
        users.setPhoneNumber(dto.getPhoneNumber());
        users.setAddress(dto.getAddress());
        users.setHash(passwordEncoder.encode(dto.getPassword()));
        users = userService.save(users);

        Role roleDefault = roleService.getRoleDefault();

        UserRole userRole = new UserRole();
        userRole.setUserId(users.getId());
        userRole.setRoleId(roleDefault.getId());
        userRole.setIsActive(Constant.STATUS.ACTIVE);
        userRoleService.save(userRole);

        return UserMapper.INSTANCE.toDto(users);
    }
}
