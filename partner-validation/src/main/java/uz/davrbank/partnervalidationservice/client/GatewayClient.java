package uz.davrbank.partnervalidationservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import uz.davrbank.partnervalidationservice.config.ApplicationProperties;
import uz.davrbank.partnervalidationservice.constants.AuthConstants;
import uz.davrbank.partnervalidationservice.exception.GatewayCommunicationException;
import uz.davrbank.partnervalidationservice.gateway.GatewayTokenService;

import java.util.concurrent.TimeoutException;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GatewayClient {

    WebClient webClient;
    GatewayTokenService gatewayTokenService;
    ApplicationProperties properties;
    GatewayResponseMapper gatewayResponseMapper;
    ObjectMapper objectMapper;

    public <T> Mono<T> exchange(
            HttpMethod method,
            String uri,
            Object requestBody,
            Class<T> responseType
    ) {
        return execute(method, uri, requestBody)
                .flatMap(rawResponse -> {
                    T response = gatewayResponseMapper.read(rawResponse, responseType);
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
                    T response = gatewayResponseMapper.read(rawResponse, responseType);
                    logMappedResponse(method, uri, response);
                    return Mono.justOrEmpty(response);
                });
    }

    private Mono<String> execute(HttpMethod method, String uri, Object requestBody) {
        return execute(method, uri, requestBody, true);
    }

    private Mono<String> execute(HttpMethod method, String uri, Object requestBody, boolean retryUnauthorized) {
        log.info(
                "Gateway request: method={}, uri={}, body={}",
                method,
                uri,
                serialize(requestBody)
        );

        WebClient.RequestHeadersSpec<?> headersSpec = buildRequest(method, uri, requestBody);
        return headersSpec.retrieve()
                .bodyToMono(String.class)
                .defaultIfEmpty("")
                .doOnNext(rawResponse -> log.info(
                        "Gateway raw response: method={}, uri={}, body={}",
                        method,
                        uri,
                        rawResponse
                ))
                .timeout(properties.gateway().requestTimeout())
                .onErrorResume(WebClientResponseException.Unauthorized.class, exception -> {
                    logGatewayErrorResponse(method, uri, exception);
                    if (!retryUnauthorized) {
                        return Mono.error(toGatewayCommunicationException(exception));
                    }
                    gatewayTokenService.evictAccessToken();
                    return execute(method, uri, requestBody, false);
                })
                .onErrorMap(WebClientResponseException.class, exception -> {
                    logGatewayErrorResponse(method, uri, exception);
                    return toGatewayCommunicationException(exception);
                })
                .onErrorMap(WebClientRequestException.class, exception -> {
                    log.error(
                            "Gateway request failed: method={}, uri={}, message={}",
                            method,
                            uri,
                            exception.getMessage(),
                            exception
                    );
                    return new GatewayCommunicationException("Gateway request failed: " + exception.getMessage(), exception);
                })
                .onErrorMap(TimeoutException.class, exception -> {
                    log.error("Gateway request timed out: method={}, uri={}", method, uri, exception);
                    return new GatewayCommunicationException("Gateway request timed out", exception);
                });
    }

    private WebClient.RequestHeadersSpec<?> buildRequest(HttpMethod method, String uri, Object requestBody) {
        String accessToken = gatewayTokenService.getAccessToken();
        WebClient.RequestBodySpec requestSpec = webClient.method(method)
                .uri(uri)
                .headers(headers -> headers.set(HttpHeaders.AUTHORIZATION, AuthConstants.BEARER_PREFIX + accessToken));
        return requestBody == null ? requestSpec : requestSpec.bodyValue(requestBody);
    }

    private GatewayCommunicationException toGatewayCommunicationException(WebClientResponseException exception) {
        return new GatewayCommunicationException(
                "Gateway responded with status %s".formatted(exception.getStatusCode()),
                exception.getStatusCode(),
                exception
        );
    }

    private void logMappedResponse(HttpMethod method, String uri, Object response) {
        log.info(
                "Gateway mapped response: method={}, uri={}, body={}",
                method,
                uri,
                serialize(response)
        );
    }

    private void logGatewayErrorResponse(HttpMethod method, String uri, WebClientResponseException exception) {
        log.error(
                "Gateway error response: method={}, uri={}, status={}, rawBody={}",
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
            log.warn("Failed to serialize gateway log payload: type={}", value.getClass().getName(), exception);
            return String.valueOf(value);
        }
    }
}
