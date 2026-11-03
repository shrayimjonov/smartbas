package uz.davrbank.userinfobase.config;


import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import uz.davrbank.userinfobase.properties.DavrhubGatewayProperties;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class WebClientConfig {

    public static final String SERVICE_NAME_HEADER = "X-Service-Name";

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean("davrhubWebClient")
    public WebClient davrhubWebClient(
            WebClient.Builder builder,
            DavrhubGatewayProperties properties
    ) {
        ConnectionProvider connectionProvider = ConnectionProvider.builder("davrhub-client-pool")
                .maxConnections(100)
                .pendingAcquireTimeout(properties.getConnectTimeout())
                .build();

        HttpClient httpClient = HttpClient.create(connectionProvider)
                .option(
                        ChannelOption.CONNECT_TIMEOUT_MILLIS,
                        Math.toIntExact(properties.getConnectTimeout().toMillis())
                )
                .responseTimeout(properties.getReadTimeout())
                .doOnConnected(connection -> connection
                        .addHandlerLast(new ReadTimeoutHandler(
                                properties.getReadTimeout().toMillis(),
                                TimeUnit.MILLISECONDS
                        ))
                        .addHandlerLast(new WriteTimeoutHandler(
                                properties.getWriteTimeout().toMillis(),
                                TimeUnit.MILLISECONDS
                        )));

        return builder
                .baseUrl(properties.getBaseUrl())
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(SERVICE_NAME_HEADER, properties.getServiceName())
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