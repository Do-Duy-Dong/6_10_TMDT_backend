package com.tmdt.shop_service.modules.users.application.service;

import com.tmdt.shop_service.modules.auth.application.dto.SignUpDto;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import com.tmdt.shop_service.modules.users.domain.model.Users;

public interface UserService {
    Users findByEmail(String email);

    Users findByPhoneNumber(String phoneNumber);

    Users save(Users users);
}
