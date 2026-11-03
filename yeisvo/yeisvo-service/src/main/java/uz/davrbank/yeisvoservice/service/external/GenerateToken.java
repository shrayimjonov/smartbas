package uz.davrbank.yeisvoservice.service.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import uz.davrbank.yeisvoservice.aggregate.JwtToken;
import uz.davrbank.yeisvoservice.exception.controller.GlobalExceptionMessage;
import uz.davrbank.yeisvoservice.aggregate.prop.ExternalProp;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
@Slf4j
@AllArgsConstructor
public class GenerateToken {
    private final ExternalConnector externalConnector;
    private final ExternalProp.ExternalConfig externalConfig;

    private JwtToken cachedToken;
    private long tokenExpireAt = 0;

    public GenerateToken(ExternalConnector externalConnector, ExternalProp.ExternalConfig yeisvo) {
        this.externalConnector = externalConnector;
        this.externalConfig = yeisvo;
    }

    /**
     * bu tokenni bean yaratilganda olib qo'yish uchun kerak
     */
    @PostConstruct
    public void init() {
        try {
            getToken();
        } catch (Exception e) {
            log.error("Error get token from GateWay msg: {}", e.getMessage());
        }
    }


    public JwtToken getToken() {
        log.info("Request to method getToken...");
        long now = System.currentTimeMillis() / 1000;

        if (cachedToken != null && now <  (tokenExpireAt - 60)) {
            log.info("Returned cashed token");
            return cachedToken;
        }

        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", externalConfig.getAuthGrantType());
            formData.add("username", externalConfig.getAuthUsername());
            formData.add("password", externalConfig.getAuthPassword());

            HttpHeaders headers = getBaseHeaders();
            String authBaseUrl = externalConfig.getAuthBaseUrl();
            log.info("authBaseUrl: {}", authBaseUrl);
            var token = this.externalConnector.exchangeWebClient(
                    authBaseUrl,
                    HttpMethod.POST,
                    headers,
                    Duration.ofSeconds(30),
                    formData,
                    new ParameterizedTypeReference<JwtToken>() {
                    }
            ).getBody();
            if (token == null || token.getAccess_token() == null) {
                if (cachedToken != null && now < tokenExpireAt) {
                    log.warn("Using LAST WORKING token because Gateway returned null");
                    return cachedToken;
                }

                throw new GlobalExceptionMessage("\"Служба GateWay не отвечает, возникла проблема с подключением. Пожалуйста, попробуйте немного позже.");
            }
            log.info("Get token from GateWay");
            cachedToken = token;
            tokenExpireAt = now + token.getExpires_in(); //exp bu shuncha vaqtdan ken tugashi . shu un hozirgi vaqtga qoshdim
            log.info("New token saved and returned");
            return token;
        } catch (Exception e) {
            String responseBody = e.getMessage();
            ObjectMapper objectMapper = new ObjectMapper();
            if (e.getMessage().contains("Timeout on blocking read for")) {
                throw new GlobalExceptionMessage("Служба GateWay не отвечает, возникла проблема с подключением!");
            }
            try {
                String absSingleResponse = objectMapper.readValue(responseBody, String.class);
                throw new GlobalExceptionMessage(absSingleResponse);
            } catch (JsonProcessingException ex) {
                throw new GlobalExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        } catch (Throwable e) {
            log.error("Error during token request: {}", e.getMessage());
            throw new GlobalExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    private HttpHeaders getBaseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Accept", "application/json");
        headers.add("Authorization", "Basic " + externalConfig.getAuthBasic());
        headers.add("Cookie", "JSESSIONID=0C1735977C296E60C23AE8E2FADA3E35");
        return headers;
    }
}

