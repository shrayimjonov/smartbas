package uz.davrbank.yeisvoservice.aggregate.config;

import lombok.extern.slf4j.Slf4j;
import uz.davrbank.yeisvoservice.service.external.ExternalConnector;
import uz.davrbank.yeisvoservice.service.external.GenerateToken;
import uz.davrbank.yeisvoservice.aggregate.prop.ExternalProp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
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
        log.info("Generating token for {}", externalProp);
        return new GenerateToken(externalConnector, externalProp.getConfig("yeisvo"));
    }
}
