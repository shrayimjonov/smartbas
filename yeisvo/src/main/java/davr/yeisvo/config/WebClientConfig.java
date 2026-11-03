package davr.yeisvo.config;

import davr.yeisvo.external.ExternalConnector;
import davr.yeisvo.external.GenerateToken;
import davr.yeisvo.prop.ExternalProp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        // Javob uchun maksimal bufer hajmini oshiramiz (masalan, 16 MB)
        int maxInMemorySize = 16 * 1024 * 1024; // 16 MB

        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                                .defaultCodecs()
                                .maxInMemorySize(maxInMemorySize))
                        .build())
                .build();
    }

    @Bean
    public GenerateToken generateToken(ExternalConnector externalConnector, ExternalProp externalProp) {
        return new GenerateToken(externalConnector, externalProp.getConfig("yeisvo"));
    }
}
