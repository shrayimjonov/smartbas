package uz.davrbank.userinfobase.gateway;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import uz.davrbank.userinfobase.client.DavrhubAccessToken;
import uz.davrbank.userinfobase.exceptions.DavrhubGatewayException;
import uz.davrbank.userinfobase.properties.DavrhubGatewayProperties;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DavrhubGatewayTokenService {

    @Qualifier("davrhubWebClient")
    WebClient webClient;
    DavrhubGatewayProperties properties;
    DavrhubTokenStoreService tokenStoreService;

    public DavrhubGatewayTokenService(WebClient webClient, DavrhubGatewayProperties properties, DavrhubTokenStoreService tokenStoreService) {
        this.webClient = webClient;
        this.properties = properties;
        this.tokenStoreService = tokenStoreService;
    }

    public String getAccessToken() {
        return findCachedToken()
                .map(DavrhubAccessToken::accessToken)
                .orElseGet(this::loadFreshAccessToken);
    }

    public void evictAccessToken() {
        tokenStoreService.evictToken();
    }

    private synchronized String loadFreshAccessToken() {
        return findCachedToken()
                .map(DavrhubAccessToken::accessToken)
                .orElseGet(() -> {
                    DavrhubGatewayTokenResponse tokenResponse = requestToken();
                    DavrhubAccessToken accessToken = tokenResponse.toAccessToken();
                    saveToken(accessToken);
                    return accessToken.accessToken();
                });
    }

    private DavrhubGatewayTokenResponse requestToken() {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("grant_type", properties.getGrantType());
        bodyBuilder.part("username", properties.getClientUsername());
        bodyBuilder.part("password", properties.getClientPassword());

        try {
            return webClient.post()
                    .uri("/api/v1/auth/oauth2/token")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .headers(headers -> headers.setBasicAuth(
                            properties.getBasicAuthUsername(),
                            properties.getBasicAuthPassword()
                    ))
                    .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                    .retrieve()
                    .bodyToMono(DavrhubGatewayTokenResponse.class)
                    .timeout(properties.getRequestTimeout())
                    .onErrorMap(TimeoutException.class,
                            exception -> new DavrhubGatewayException("Davrhub token request timed out", exception))
                    .blockOptional()
                    .orElseThrow(() -> new DavrhubGatewayException("Davrhub token response was empty"));
        } catch (WebClientResponseException exception) {
            throw new DavrhubGatewayException(
                    "Davrhub token endpoint responded with status %s".formatted(exception.getStatusCode()),
                    exception
            );
        } catch (WebClientRequestException exception) {
            throw new DavrhubGatewayException(
                    "Davrhub token request failed: " + exception.getMessage(),
                    exception
            );
        }
    }

    private Optional<DavrhubAccessToken> findCachedToken() {
        return tokenStoreService.getToken();
    }

    private void saveToken(DavrhubAccessToken accessToken) {
        tokenStoreService.storeToken(
                accessToken,
                Duration.ofSeconds(resolveTtl(accessToken.expiresInSeconds()))
        );
    }

    private long resolveTtl(long expiresInSeconds) {
        Duration tokenDuration = Duration.ofSeconds(Math.max(expiresInSeconds, 1));
        Duration adjusted = tokenDuration.minus(properties.getTokenExpirySkew());
        Duration ttl = adjusted.isNegative() || adjusted.isZero() ? tokenDuration : adjusted;
        return ttl.toSeconds();
    }
}