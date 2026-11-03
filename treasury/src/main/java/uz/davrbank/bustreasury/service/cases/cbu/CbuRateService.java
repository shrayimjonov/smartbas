package uz.davrbank.bustreasury.service.cases.cbu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import uz.davrbank.bustreasury.aggregate.dto.auth.AuthTokenDto;
import uz.davrbank.bustreasury.aggregate.dto.response.cbu.CurrencyRateResponseDTO;
import uz.davrbank.bustreasury.service.interfaces.AuthTokenInterface;
import uz.davrbank.bustreasury.service.interfaces.CBUCallerInterface;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

@Service
public class CbuRateService implements CBUCallerInterface {

    private final RestTemplate restTemplate;
    private final WebClient webClient;
    private final AuthTokenInterface authTokenInterface;

    private static final Logger LOG = LoggerFactory.getLogger(CbuRateService.class);

    @Value("${cbu.currency.api.url}")
    private String centralBankApiUrl;

    public CbuRateService(RestTemplate restTemplate, WebClient webClient, AuthTokenInterface authTokenInterface) {
        this.restTemplate = restTemplate;
        this.webClient = webClient;
        this.authTokenInterface = authTokenInterface;
    }

    public BigDecimal getLatestRate(String ccy) {
        try {
            ResponseEntity<CurrencyRateResponseDTO[]> resp =
                    restTemplate.getForEntity(centralBankApiUrl, CurrencyRateResponseDTO[].class);
            var body = resp.getBody();
            if (body == null || body.length == 0) {
                throw new IllegalStateException("CBU response empty");
            }

            return Arrays.stream(body)
                    .filter(r -> ccy.equalsIgnoreCase(r.getCcy()))
                    .findFirst()
                    .map(r -> new BigDecimal(Objects.requireNonNull(r.getRate())))
                    .orElseThrow(() -> new IllegalArgumentException("Currency not found in CBU: " + ccy));

        } catch (Exception e) {
            LOG.error("CBU rate fetch failed: {}", e.getMessage(), e);
            throw new RuntimeException("CBU rate fetch failed: " + e.getMessage(), e);
        }
    }

    @Override
    public CurrencyRateResponseDTO[] getLatestRateUpdate() {
        try {
            AuthTokenDto tokenDto = authTokenInterface.getAuthToken();
            String accessToken = tokenDto.getAccessToken();

            return fetchRatesWithToken(accessToken);

        } catch (WebClientResponseException.Unauthorized e) {
            LOG.warn("Rates call 401 Unauthorized — retry with fresh token...");
            AuthTokenDto tokenDto = authTokenInterface.getAuthToken();
            return fetchRatesWithToken(tokenDto.getAccessToken());

        } catch (Exception e) {
            LOG.error("CBU rate fetch failed: {}", e.getMessage(), e);
            throw new RuntimeException("CBU rate fetch failed: " + e.getMessage(), e);
        }
    }


    private CurrencyRateResponseDTO[] fetchRatesWithToken(String accessToken) {
        return webClient.get()
                .uri(centralBankApiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(CurrencyRateResponseDTO[].class)
                .block();
    }
}
