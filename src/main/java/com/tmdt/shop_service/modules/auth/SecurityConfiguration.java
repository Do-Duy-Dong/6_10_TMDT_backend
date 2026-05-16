package com.tmdt.shop_service.modules.auth;

import com.tmdt.shop_service.utils.CorsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    final CorsProperties corsProperties;
    final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    public List<String> permitUrls = List.of(
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v1/auth/**",
            "/v1/upload/**",
            "/v1/public/**");

    public SecurityConfiguration(CorsProperties corsConfiguration, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter) {
        this.corsProperties = corsConfiguration;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(requests -> {
                    requests.requestMatchers(permitUrls.toArray(new String[0])).permitAll();
                    requests.requestMatchers("/v1/admin/**").hasAuthority("ROLE_ADMIN");
                    requests.anyRequest().authenticated();
                })
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginProcessingUrl("/v1/auth/auth-url").defaultSuccessUrl("http://localhost:3000/"));

        http.addFilterBefore(jwtAuthenticationTokenFilter, BearerTokenAuthenticationFilter.class);
        http.addFilterBefore(new ApiKeyAuthenticationFilter(), JwtAuthenticationTokenFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordBCryptEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Allow specific origins
        config.setAllowedOrigins(
                List.of(corsProperties.getAllowedOrigins().toArray(new String[0])));

        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        // Add exposed headers if needed
        config.setExposedHeaders(List.of("Authorization", "Content-Type"));

        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
