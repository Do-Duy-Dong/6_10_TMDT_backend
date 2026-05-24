package com.tmdt.shop_service.modules.auth.infrastucture.controller;

import com.tmdt.shop_service.modules.auth.application.request.GoogleSsoRequest;
import com.tmdt.shop_service.modules.auth.application.service.SsoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth/sso")
@RequiredArgsConstructor
public class SsoOauthController {
    final SsoService ssoService;
    @GetMapping
    public ResponseEntity<String> loginType(@RequestParam(name = "login-type") String loginType) {
        String oauthUrl = ssoService.getAuthorizationUrl(loginType);
        return ResponseEntity.ok(oauthUrl);
    }

    @PostMapping("/google-sso")
    public ResponseEntity<Object> googleSso(@RequestBody GoogleSsoRequest request) {
        Object result = ssoService.googleVerify(request);
        return ResponseEntity.ok(result);
    }
}
