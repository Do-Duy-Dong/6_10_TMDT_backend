package com.tmdt.shop_service.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.util.TimeZone;

@Configuration
public class BindObjectConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper().setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")));
    }
}
