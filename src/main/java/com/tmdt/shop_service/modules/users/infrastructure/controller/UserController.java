package com.tmdt.shop_service.modules.users.infrastructure.controller;

import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import com.tmdt.shop_service.modules.users.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {
    final UserService userService;

    @GetMapping("/profile")
    public UserDto getInfor(@AuthenticationPrincipal CustomUserDetail userDetail) {
        return userService.getProfile(userDetail.getId());
    }
}
