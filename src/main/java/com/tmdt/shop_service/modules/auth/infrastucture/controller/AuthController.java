package com.tmdt.shop_service.modules.auth.infrastucture.controller;

import com.tmdt.shop_service.modules.auth.application.dto.SignUpDto;
import com.tmdt.shop_service.modules.auth.application.service.AuthService;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Tag(name = "Auth api")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody SignUpDto dto) {
        UserDto userDto =  authService.signUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }
}
