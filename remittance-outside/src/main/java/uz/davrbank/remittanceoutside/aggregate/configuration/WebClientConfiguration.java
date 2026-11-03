/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.aggregate.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import uz.davrbank.remittanceoutside.aggregate.aop.ApiExchangeLogger;
import uz.davrbank.remittanceoutside.aggregate.aop.WebclientLoggingFilter;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;


@Configuration
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class WebClientConfiguration {

    ApplicationProperties applicationProperties;

    @Bean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean(name = "leskarClient")
    public WebClient leskarClient(ApiExchangeLogger apiExchangeLogger) {
        var executor = Executors.newFixedThreadPool(20);
        HttpClient httpClient = HttpClient.newBuilder()
                .executor(executor)
                .connectTimeout(Duration.ofSeconds(30))
                .build();
        return WebClient.builder()
                .clientConnector(new JdkClientHttpConnector(httpClient))
                .baseUrl(applicationProperties.getLeskar().getBaseUrl())
                .filter(WebclientLoggingFilter.requestResponseLogging(apiExchangeLogger))
                .build();
    }

    @Bean("leskarIntegrationObjectMapper")
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
                .configure(JsonGenerator.Feature.ESCAPE_FORWARD_SLASHES, false);
    }
}