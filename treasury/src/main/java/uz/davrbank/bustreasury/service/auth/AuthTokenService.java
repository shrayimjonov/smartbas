package uz.davrbank.bustreasury.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import uz.davrbank.bustreasury.aggregate.dto.auth.AuthTokenDto;
import uz.davrbank.bustreasury.exeption.AuthTokenException;
import uz.davrbank.bustreasury.service.interfaces.AuthTokenInterface;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@Service
public class AuthTokenService implements AuthTokenInterface {
    private final WebClient webClient;
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);

    @Value("${security.service.username}")
    private String basicUsername;

    @Value("${security.service.password}")
    private String basicPassword;

    @Value("${security.service.client.username}")
    private String oauthUsername;

    @Value("${security.service.client.password}")
    private String oauthPassword;

    @Value("${security.service.url}")
    private String baseUrl;

    @Value("${security.service.auth.endpoint}")
    private String authEndpoint;

    public AuthTokenService(@Qualifier("getWebClientAuth") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public AuthTokenDto getAuthToken() {
        Date date = new Date();
        log.info(" ... GENERATING TOKEN  ...{}", formatter.format(date));
        try {
            AuthTokenDto tokenDTO = Objects.requireNonNull(webClient
                            .post()
                            .uri(baseUrl + authEndpoint)
                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                            .headers(httpHeaders -> {
                                httpHeaders.setBasicAuth(oauthUsername, oauthPassword);
                                httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
                            })
                            .body(BodyInserters.fromFormData("grant_type", "password")
                                    .with("username", basicUsername)
                                    .with("password", basicPassword))
                            .retrieve())
                    .bodyToMono(AuthTokenDto.class)
                    .timeout(Duration.ofSeconds(30))
                    .retryWhen(Retry.backoff(5, Duration.ofSeconds(2))
                            .doBeforeRetry(signal -> log.warn("Retrying auth token request, attempt: {}, cause: {}",
                                    signal.totalRetries() + 1, signal.failure().getMessage())))
                    .onErrorResume(throwable -> {
                        log.error("Auth token request failed: {}", throwable.getMessage());
                        return Mono.error(new AuthTokenException("Failed to obtain auth token: " + throwable.getMessage()));
                    })
                    .block();
            log.info("Token successfully taken from 3rdParty!");
            return tokenDTO;
        } catch (Exception ex) {
            log.error("Getting auth token exception, cause: {}", ex.getMessage());
            if (ex.getCause() != null && ex.getCause().getMessage().contains("400")) {
                log.error("400 Bad Request detected, check request parameters or server requirements: {}", ex.getCause().getMessage());
            }
            throw new AuthTokenException("Failed to obtain auth token: " + ex.getMessage());
        }
    }
}
