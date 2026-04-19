package com.tmdt.shop_service.modules.auth.application.service;

import com.tmdt.shop_service.modules.auth.application.dto.SignUpDto;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;

public interface AuthService {
    UserDto signUp(SignUpDto dto);
}
