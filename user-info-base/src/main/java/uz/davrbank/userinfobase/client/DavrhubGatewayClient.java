package uz.davrbank.userinfobase.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import uz.davrbank.userinfobase.exceptions.DavrhubGatewayException;
import uz.davrbank.userinfobase.gateway.DavrhubGatewayTokenService;
import uz.davrbank.userinfobase.properties.DavrhubGatewayProperties;

import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DavrhubGatewayClient {

    public static final String BEARER_PREFIX = "Bearer ";

    @Qualifier("davrhubWebClient")
    WebClient webClient;
    DavrhubGatewayTokenService davrhubGatewayTokenService;
    DavrhubGatewayProperties properties;
    DavrhubGatewayResponseMapper davrhubGatewayResponseMapper;
    ObjectMapper objectMapper;

    public DavrhubGatewayClient(WebClient webClient, DavrhubGatewayTokenService davrhubGatewayTokenService,
                                DavrhubGatewayProperties properties, DavrhubGatewayResponseMapper davrhubGatewayResponseMapper,
                                ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.davrhubGatewayTokenService = davrhubGatewayTokenService;
        this.properties = properties;
        this.davrhubGatewayResponseMapper = davrhubGatewayResponseMapper;
        this.objectMapper = objectMapper;
    }

    public <T> Mono<T> exchange(
            HttpMethod method,
            String uri,
            Object requestBody,
            Class<T> responseType
    ) {
        return execute(method, uri, requestBody)
                .flatMap(rawResponse -> {
                    T response = davrhubGatewayResponseMapper.read(rawResponse, responseType);
                    logMappedResponse(method, uri, response);
                    return Mono.justOrEmpty(response);
                });
    }

    public <T> Mono<T> exchange(
            HttpMethod method,
            String uri,
            Object requestBody,
            ParameterizedTypeReference<T> responseType
    ) {
        return execute(method, uri, requestBody)
                .flatMap(rawResponse -> {
                    T response = davrhubGatewayResponseMapper.read(rawResponse, responseType);
                    logMappedResponse(method, uri, response);
                    return Mono.justOrEmpty(response);
                });
    }

    public Flux<DataBuffer> stream(HttpMethod method, String uri, Object requestBody) {
        return executeStream(method, uri, requestBody, true);
    }

    private Mono<String> execute(HttpMethod method, String uri, Object requestBody) {
        return execute(method, uri, requestBody, true);
    }

    private Mono<String> execute(HttpMethod method, String uri, Object requestBody, boolean retryUnauthorized) {
        log.info("Davrhub request: method={}, uri={}, body={}", method, uri, serialize(requestBody));

        WebClient.RequestHeadersSpec<?> headersSpec = buildRequest(method, uri, requestBody);
        return headersSpec.exchangeToMono(response -> {
                    log.info("Davrhub response: method={}, uri={}, status={}", method, uri, response.statusCode());
                    if (response.statusCode().isError()) {
                        return response.createException().flatMap(Mono::error);
                    }
                    return response.bodyToMono(String.class).defaultIfEmpty("");
                })
                .doOnNext(rawResponse -> log.info(
                        "Davrhub raw response: method={}, uri={}, body={}",
                        method,
                        uri,
                        rawResponse
                ))
                .timeout(properties.getRequestTimeout())
                .onErrorResume(WebClientResponseException.Unauthorized.class, exception -> {
                    logGatewayErrorResponse(method, uri, exception);
                    if (!retryUnauthorized) {
                        return Mono.error(toDavrhubGatewayException(exception));
                    }
                    davrhubGatewayTokenService.evictAccessToken();
                    return execute(method, uri, requestBody, false);
                })
                .onErrorMap(WebClientResponseException.class, exception -> {
                    logGatewayErrorResponse(method, uri, exception);
                    return toDavrhubGatewayException(exception);
                })
                .onErrorMap(WebClientRequestException.class, exception -> {
                    log.error(
                            "Davrhub request failed: method={}, uri={}, message={}",
                            method,
                            uri,
                            exception.getMessage(),
                            exception
                    );
                    return new DavrhubGatewayException("Davrhub request failed: " + exception.getMessage(), exception);
                })
                .onErrorMap(TimeoutException.class, exception -> {
                    log.error("Davrhub request timed out: method={}, uri={}", method, uri, exception);
                    return new DavrhubGatewayException("Davrhub request timed out", exception);
                });
    }

    private Flux<DataBuffer> executeStream(
            HttpMethod method,
            String uri,
            Object requestBody,
            boolean retryUnauthorized
    ) {
        log.info("Davrhub stream request: method={}, uri={}, body={}", method, uri, serialize(requestBody));

        WebClient.RequestHeadersSpec<?> headersSpec = buildRequest(method, uri, requestBody);
        return headersSpec.exchangeToFlux(response -> {
                    log.info("Davrhub stream response: method={}, uri={}, status={}", method, uri, response.statusCode());
                    if (response.statusCode().isError()) {
                        return response.createException().flatMapMany(Flux::error);
                    }
                    return response.bodyToFlux(DataBuffer.class);
                })
                .onErrorResume(WebClientResponseException.Unauthorized.class, exception -> {
                    logGatewayErrorResponse(method, uri, exception);
                    if (!retryUnauthorized) {
                        return Flux.error(toDavrhubGatewayException(exception));
                    }
                    davrhubGatewayTokenService.evictAccessToken();
                    return executeStream(method, uri, requestBody, false);
                })
                .onErrorMap(WebClientResponseException.class, exception -> {
                    logGatewayErrorResponse(method, uri, exception);
                    return toDavrhubGatewayException(exception);
                })
                .onErrorMap(WebClientRequestException.class, exception -> {
                    log.error(
                            "Davrhub stream request failed: method={}, uri={}, message={}",
                            method,
                            uri,
                            exception.getMessage(),
                            exception
                    );
                    return new DavrhubGatewayException(
                            "Davrhub stream request failed: " + exception.getMessage(),
                            exception
                    );
                })
                .onErrorMap(TimeoutException.class, exception -> {
                    log.error("Davrhub stream request timed out: method={}, uri={}", method, uri, exception);
                    return new DavrhubGatewayException("Davrhub stream request timed out", exception);
                });
    }

    private WebClient.RequestHeadersSpec<?> buildRequest(HttpMethod method, String uri, Object requestBody) {
        String accessToken = davrhubGatewayTokenService.getAccessToken();
        WebClient.RequestBodySpec requestSpec = webClient.method(method)
                .uri(uri)
                .headers(headers -> headers.set(
                        HttpHeaders.AUTHORIZATION,
                        BEARER_PREFIX + accessToken
                ));
        return requestBody == null ? requestSpec : requestSpec.bodyValue(requestBody);
    }

    private DavrhubGatewayException toDavrhubGatewayException(WebClientResponseException exception) {
        return new DavrhubGatewayException(
                "Davrhub gateway responded with status %s".formatted(exception.getStatusCode()),
                exception
        );
    }

    private void logMappedResponse(HttpMethod method, String uri, Object response) {
        log.info(
                "Davrhub mapped response: method={}, uri={}, body={}",
                method,
                uri,
                serialize(response)
        );
    }

    private void logGatewayErrorResponse(HttpMethod method, String uri, WebClientResponseException exception) {
        log.error(
                "Davrhub error response: method={}, uri={}, status={}, rawBody={}",
                method,
                uri,
                exception.getStatusCode(),
                exception.getResponseBodyAsString(),
                exception
        );
    }

    private String serialize(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String stringValue) {
            return stringValue;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException exception) {
            log.warn("Failed to serialize Davrhub log payload: type={}", value.getClass().getName(), exception);
            return String.valueOf(value);
        }
    }
}
