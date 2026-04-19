package com.tmdt.shop_service.modules.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    private final List<String> permitUrls = List.of(
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v1/auth/**",
            "/v1/public/**");

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        sessions -> sessions.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(permitUrls.toArray(new String[0])).permitAll();
                    requests.requestMatchers("/v1/admin/**").hasAuthority("ROLE_ADMIN");
                    requests.anyRequest().authenticated();
                });

        http.addFilterBefore(new ApiKeyAuthenticationFilter(), BearerTokenAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordBCryptEncoder() {
        return new BCryptPasswordEncoder();
    }
}
