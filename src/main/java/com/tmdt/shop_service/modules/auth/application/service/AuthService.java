package com.tmdt.shop_service.modules.auth.application.service;

import com.tmdt.shop_service.modules.auth.application.dto.AuthTokenDto;
import com.tmdt.shop_service.modules.auth.application.dto.SignUpDto;
import com.tmdt.shop_service.modules.auth.application.request.LoginRequest;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import com.tmdt.shop_service.modules.users.domain.model.Users;

public interface AuthService {
    UserDto signUp(SignUpDto dto);

    AuthTokenDto login(LoginRequest request);

    AuthTokenDto refreshToken(Long userId);

    AuthTokenDto generateToken(Users users);
}
