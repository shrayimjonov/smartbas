package davr.yeisvo.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import davr.yeisvo.dto.JwtToken;
import davr.yeisvo.exceptionTemp.GlobalExceptionMessage;
import davr.yeisvo.prop.ExternalProp;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.Duration;

@AllArgsConstructor
public class GenerateToken {
    private final ExternalConnector externalConnector;
    private final ExternalProp.ExternalConfig externalConfig;
    private final Logger logger = LogManager.getFormatterLogger(YeisvoRequestService.class);

    public JwtToken getToken() {

        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("grant_type", externalConfig.getAuthGrantType());
            formData.add("username", externalConfig.getAuthUsername());
            formData.add("password", externalConfig.getAuthPassword());
            HttpHeaders headers = getBaseHeaders();

            return this.externalConnector.exchangeWebClient(
                    externalConfig.getAuthBaseUrl(),
                    HttpMethod.POST,
                    headers,
                    Duration.ofSeconds(30),
                    formData,
                    new ParameterizedTypeReference<JwtToken>() {}
            ).getBody();

        } catch (Exception e) {
//            if (e.getRawStatusCode() == 401) {
//                throw new GlobalExceptionMessage(HttpStatus.UNAUTHORIZED.getReasonPhrase());
//            }
            String responseBody = e.getMessage();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String absSingleResponse = objectMapper.readValue(responseBody, String.class);
                throw new GlobalExceptionMessage(absSingleResponse);
            } catch (JsonProcessingException ex) {
                throw new GlobalExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        } catch (Throwable e) {
            logger.error("Error during token request: {}", e.getMessage());
            throw new GlobalExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    private HttpHeaders getBaseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Accept", "application/json");
        headers.add("Authorization", "Basic "+ externalConfig.getAuthBasic());
        headers.add("Cookie", "JSESSIONID=REDACTED; JSESSIONID=REDACTED");
        return headers;
    }


}

