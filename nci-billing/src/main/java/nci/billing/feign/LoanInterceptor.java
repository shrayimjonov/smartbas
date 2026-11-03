package nci.billing.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import nci.billing.model.property.LoanProps;
import nci.billing.model.request.loan.auth.AuthResponse;
import nci.billing.model.request.loan.auth.RefreshRequest;
import nci.billing.model.request.loan.auth.SignRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class LoanInterceptor implements RequestInterceptor {
    private final LoanProps loanProps;
    private final RestTemplate restTemplate;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer ";
    private static final String AUTH_URL = "/auth/token";
    private AuthResponse authResponse;
    private final SignRequest signRequest;
    private long expireTimeToken;

    public LoanInterceptor(RestTemplate restTemplate, LoanProps loanProps) {
        this.restTemplate = restTemplate;
        this.loanProps = loanProps;
        this.signRequest = new SignRequest("password", loanProps.getUsername(), loanProps.getPassword());
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Content-Type", "application/json");
        requestTemplate.header("Accept", "application/json");
        requestTemplate.header(AUTHORIZATION_HEADER, TOKEN_TYPE + getAccessToken());
    }

    private String getAccessToken() {
        if (authResponse == null || authResponse.getAccessToken() == null) {
            log.info("""
                    auth response is null

                    >>> will be sent new request to Token""");
            requestNewToken();
        } else if (isExpired()) {
            log.info("""
                    token is expired

                     >>> will be sent refresh Token request""");
            refreshToken();
        }
        return authResponse.getAccessToken();
    }

    private boolean isExpired() {
        long ts = System.currentTimeMillis();
        log.debug("Billing expTime {}, Current TS {}", expireTimeToken, ts);
        return expireTimeToken - 60000 <= ts; /* check token before expires (in 59 minute) */

    }


    private void requestNewToken() {
        log.info("Starting Request to get new Access token from Loan-Service 127.0.0.1");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<SignRequest> request = new HttpEntity<>(signRequest, headers);
        authResponse = restTemplate.postForEntity(loanProps.getUrl() + AUTH_URL, request, AuthResponse.class).getBody();
        expireTimeToken = System.currentTimeMillis() + authResponse.getExpiresIn() * 1000L;
    }

    private void refreshToken() {
        log.info("Starting Refresh token from Loan-Service 127.0.0.1");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var refreshRequest = new RefreshRequest("refresh_token",
                authResponse.getAccessToken(),
                authResponse.getRefreshToken());

        log.info("Refresh token prepared request: {}", refreshRequest);

        HttpEntity<RefreshRequest> request = new HttpEntity<>(
                refreshRequest,
                headers
        );


        authResponse = restTemplate.postForEntity(loanProps.getUrl() + AUTH_URL, request, AuthResponse.class).getBody();
        expireTimeToken = System.currentTimeMillis() + authResponse.getExpiresIn() * 1000L;
    }

}
