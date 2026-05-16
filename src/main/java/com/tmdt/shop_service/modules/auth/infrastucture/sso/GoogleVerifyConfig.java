package com.tmdt.shop_service.modules.auth.infrastucture.sso;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class GoogleVerifyConfig {
    final ClientRegistrationRepository clientRegistrationRepository;

    @Bean
    public GoogleIdTokenVerifier googleIdTokenVerifier() {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");

        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(clientRegistration.getClientId()))
                .build();
    }
}
