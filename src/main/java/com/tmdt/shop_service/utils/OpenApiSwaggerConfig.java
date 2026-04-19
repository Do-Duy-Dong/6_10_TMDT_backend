package com.tmdt.shop_service.utils;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OpenApiSwaggerConfig {
    private static final String BASIC_SCHEME_NAME = "Basic Schema";

    @Bean
    public OpenAPI customOpenAPI() {
        var openAPI = new OpenAPI();
        openAPI.components(
                new Components().addSecuritySchemes(BASIC_SCHEME_NAME, createBasicServiceAuth()))
                .addSecurityItem(new SecurityRequirement().addList(BASIC_SCHEME_NAME))
                .info(
                        new Info()
                                .title("Shop Laptop Service")
                                .description("List api for service")
                                .version("1.0"));
        return openAPI;
    }

    private SecurityScheme createBasicServiceAuth() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .name("X-API-KEY")
                .in(SecurityScheme.In.HEADER);
    }
}
