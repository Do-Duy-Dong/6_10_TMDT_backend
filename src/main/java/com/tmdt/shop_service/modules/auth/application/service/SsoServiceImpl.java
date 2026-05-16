package com.tmdt.shop_service.modules.auth.application.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.tmdt.shop_service.modules.auth.application.dto.AuthTokenDto;
import com.tmdt.shop_service.modules.auth.application.dto.RegisterDto;
import com.tmdt.shop_service.modules.auth.application.request.GoogleSsoRequest;
import com.tmdt.shop_service.modules.auth.infrastucture.sso.GoogleSso;
import com.tmdt.shop_service.modules.users.application.service.UserService;
import com.tmdt.shop_service.modules.users.domain.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SsoServiceImpl implements SsoService {
    final GoogleSso googleSso;
    final UserService userService;

    @Override
    public String getAuthorizationUrl(String logicType) {
        if (Objects.equals(logicType, "google")) {
            return googleSso.getAuthorizationUrl();
        } else {
            throw new IllegalArgumentException("invalid logicType");
        }
    }

    @Override
    public Object googleVerify(GoogleSsoRequest request) {
        GoogleIdToken.Payload payload = googleSso.getEmailUserFromRequestSso(request);
        Users users = userService.findByEmail(payload.getEmail());
        if (users == null) {
            return RegisterDto.builder()
                    .email(payload.getEmail())
                    .fullName(payload.get("name").toString())
                    .build();
        }

        return users;
    }
}
