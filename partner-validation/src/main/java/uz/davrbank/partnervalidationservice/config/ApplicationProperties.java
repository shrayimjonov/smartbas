package uz.davrbank.partnervalidationservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app")
public record ApplicationProperties(
        GatewayProperties gateway
) {

    public record GatewayProperties(
            String baseUrl,
            String clientUsername,
            String clientPassword,
            String basicAuthUsername,
            String basicAuthPassword,
            Duration connectTimeout,
            Duration readTimeout,
            Duration writeTimeout,
            Duration requestTimeout,
            String grantType,
            Duration tokenExpirySkew
    ) {
    }

}
