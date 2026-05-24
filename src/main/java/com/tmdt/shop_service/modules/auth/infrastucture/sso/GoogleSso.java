package com.tmdt.shop_service.modules.auth.infrastucture.sso;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.tmdt.shop_service.core.exception.ForbiddenException;
import com.tmdt.shop_service.core.exception.InternalException;
import com.tmdt.shop_service.modules.auth.application.request.GoogleSsoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GoogleSso {
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final GoogleIdTokenVerifier googleIdTokenVerifier;

    public String getAuthorizationUrl() {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        
        if (clientRegistration == null) {
            throw new IllegalArgumentException("Google client registration not found");
        }

        OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
                .authorizationUri(clientRegistration.getProviderDetails().getAuthorizationUri())
                .clientId(clientRegistration.getClientId())
                .scopes(clientRegistration.getScopes())
                .state(UUID.randomUUID().toString())
                .redirectUri(clientRegistration.getRedirectUri())
                .build();

        return authorizationRequest.getAuthorizationRequestUri();
    }

    public GoogleIdToken.Payload getEmailUserFromRequestSso(GoogleSsoRequest request) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        if (clientRegistration == null) {
            throw new InternalException("Google registration not found");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("code", request.getCode());
        params.put("client_id", clientRegistration.getClientId());
        params.put("client_secret", clientRegistration.getClientSecret());
        params.put("redirect_uri", clientRegistration.getRedirectUri()); // Phải khớp với FE
        params.put("grant_type", "authorization_code");

        try {
            RestTemplate restTemplate = new RestTemplate();

            Map<String, Object> response = restTemplate.postForObject(
                    "https://oauth2.googleapis.com/token", params, Map.class);

            String idToken = null;
            if (response != null &&  response.get("id_token") != null) {
                idToken = (String) response.get("id_token");
            }

            GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(idToken);
            Optional<GoogleIdToken.Payload> payload = Optional.empty();
            if (googleIdToken != null) {
                payload = Optional.ofNullable(googleIdToken.getPayload());
            }

            return payload.orElseThrow(() -> new InternalException("Invalid idToken"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
