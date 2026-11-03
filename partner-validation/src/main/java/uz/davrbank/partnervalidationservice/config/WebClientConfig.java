package uz.davrbank.partnervalidationservice.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import uz.davrbank.partnervalidationservice.constants.AuthConstants;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

@Slf4j
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder, ApplicationProperties properties) {
        ApplicationProperties.GatewayProperties gatewayProperties = properties.gateway();
        ConnectionProvider connectionProvider = ConnectionProvider.builder("gateway-client-pool")
                .maxConnections(100)
                .pendingAcquireTimeout(gatewayProperties.connectTimeout())
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(
                        ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        Math.toIntExact(gatewayProperties.connectTimeout().toMillis())
                )
                .responseTimeout(gatewayProperties.readTimeout())
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(
                                gatewayProperties.readTimeout().toMillis(),
                                TimeUnit.MILLISECONDS
                        ))
                        .addHandlerLast(new WriteTimeoutHandler(
                                gatewayProperties.writeTimeout().toMillis(),
                                TimeUnit.MILLISECONDS
                        )));

        return builder
                .baseUrl(gatewayProperties.baseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(AuthConstants.SERVICE_NAME_HEADER, "partner-validation-service")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter(outboundLoggingFilter())
                .build();
    }

    private ExchangeFilterFunction outboundLoggingFilter() {
        return ExchangeFilterFunction.ofRequestProcessor(request -> {
            log.info("Outbound request: method={}, url={}", request.method(), request.url());
            return Mono.just(request);
        }).andThen(ExchangeFilterFunction.ofResponseProcessor(response -> {
            log.info("Outbound response: status={}", response.statusCode());
            return Mono.just(response);
        }));
    }
}
