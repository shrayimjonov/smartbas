package uz.davrbank.partnervalidationservice.gateway;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import uz.davrbank.partnervalidationservice.client.GatewayAccessToken;
import uz.davrbank.partnervalidationservice.config.ApplicationProperties;
import uz.davrbank.partnervalidationservice.exception.GatewayCommunicationException;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GatewayTokenService {

    WebClient webClient;
    ApplicationProperties properties;
    TokenStoreService tokenStoreService;

    public String getAccessToken() {
        return findCachedToken()
                .map(GatewayAccessToken::accessToken)
                .orElseGet(this::loadFreshAccessToken);
    }

    public void evictAccessToken() {
        tokenStoreService.evictToken();
    }

    private synchronized String loadFreshAccessToken() {
        return findCachedToken()
                .map(GatewayAccessToken::accessToken)
                .orElseGet(() -> {
                    GatewayTokenResponse tokenResponse = requestToken();
                    GatewayAccessToken accessToken = tokenResponse.toAccessToken();
                    saveToken(accessToken);
                    return accessToken.accessToken();
                });
    }

    private GatewayTokenResponse requestToken() {
        ApplicationProperties.GatewayProperties gatewayProperties = properties.gateway();
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("grant_type", gatewayProperties.grantType());
        bodyBuilder.part("username", gatewayProperties.clientUsername());
        bodyBuilder.part("password", gatewayProperties.clientPassword());

        try {
            return webClient.post()
                    .uri("/api/v1/auth/oauth2/token")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .headers(headers -> {
                        headers.setBasicAuth(
                                gatewayProperties.basicAuthUsername(),
                                gatewayProperties.basicAuthPassword()
                        );
                    })
                    .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                    .retrieve()
                    .bodyToMono(GatewayTokenResponse.class)
                    .timeout(gatewayProperties.requestTimeout())
                    .onErrorMap(java.util.concurrent.TimeoutException.class, exception ->
                            new GatewayCommunicationException("Gateway token request timed out", exception))
                    .blockOptional()
                    .orElseThrow(() -> new GatewayCommunicationException("Gateway token response was empty"));
        } catch (WebClientResponseException exception) {
            throw new GatewayCommunicationException(
                    "Gateway token endpoint responded with status %s".formatted(exception.getStatusCode()),
                    exception.getStatusCode(),
                    exception
            );
        } catch (WebClientRequestException exception) {
            throw new GatewayCommunicationException("Gateway token request failed: " + exception.getMessage(), exception);
        }
    }

    private Optional<GatewayAccessToken> findCachedToken() {
        return tokenStoreService.getToken();
    }

    private void saveToken(GatewayAccessToken accessToken) {
        tokenStoreService.storeToken(accessToken, Duration.ofSeconds(resolveTtl(accessToken.expiresInSeconds())));
    }

    private long resolveTtl(long expiresInSeconds) {
        Duration tokenDuration = Duration.ofSeconds(Math.max(expiresInSeconds, 1));
        Duration adjusted = tokenDuration.minus(properties.gateway().tokenExpirySkew());
        Duration ttl = adjusted.isNegative() || adjusted.isZero() ? tokenDuration : adjusted;
        return ttl.toSeconds();
    }
}
