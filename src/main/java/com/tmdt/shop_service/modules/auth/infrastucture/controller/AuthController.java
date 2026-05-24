package com.tmdt.shop_service.modules.auth.infrastucture.controller;

import com.tmdt.shop_service.modules.auth.CustomUserDetail;
import com.tmdt.shop_service.modules.auth.application.dto.AuthTokenDto;
import com.tmdt.shop_service.modules.auth.application.dto.SignUpDto;
import com.tmdt.shop_service.modules.auth.application.request.LoginRequest;
import com.tmdt.shop_service.modules.auth.application.service.AuthService;
import com.tmdt.shop_service.modules.users.application.dto.UserDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<AuthTokenDto> login(@Valid @RequestBody LoginRequest request) {
        var result = authService.login(request);

        return  ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/token")
    public ResponseEntity<AuthTokenDto> refreshToken(@AuthenticationPrincipal CustomUserDetail userDetail) {
        var result = authService.refreshToken(userDetail.getId());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
