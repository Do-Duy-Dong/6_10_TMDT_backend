package com.tmdt.shop_service.modules.payment.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "sepay")
public class SepayConfig {
    String merchantId;
    String secretKey;
    String xApiKeyIpn;
    String baseUrl;
    String uriInit;
}
