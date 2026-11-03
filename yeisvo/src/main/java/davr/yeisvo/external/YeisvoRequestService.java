package davr.yeisvo.external;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import davr.yeisvo.exceptionTemp.GlobalExceptionMessage;
import davr.yeisvo.prop.ExternalProp;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.nio.charset.StandardCharsets;
import java.time.Duration;



public abstract class YeisvoRequestService {


    private final Logger logger = LogManager.getFormatterLogger(YeisvoRequestService.class);

    private final ExternalProp.ExternalConfig externalConfig;
    private final ExternalConnector externalConnector;
    protected final GenerateToken generateToken;

    protected YeisvoRequestService(ExternalProp.ExternalConfig externalConfig, ExternalConnector externalConnector, GenerateToken generateToken) {
        this.externalConfig = externalConfig;
        this.externalConnector = externalConnector;
        this.generateToken = generateToken;
    }

    public <REQ, RES> RES externalCallPost(String url, REQ requestBody, ParameterizedTypeReference<RES> responseType){
        return externalCall(url,HttpMethod.POST,requestBody, responseType);

    }
    public <REQ, RES> RES externalCall(String url,HttpMethod httpMethod,  REQ requestBody, ParameterizedTypeReference<RES> responseType) {
        HttpHeaders headers = this.getBaseHeaders();
        try {
            return this.externalConnector.exchangeWebClient(
                    getUrl(url),
                    httpMethod,
                    headers,
                    Duration.ofSeconds(100),
                    requestBody,
                    responseType).getBody();

        } catch (WebClientResponseException e) {
            if (e.getRawStatusCode() == 401) {
                throw new GlobalExceptionMessage(HttpStatus.UNAUTHORIZED.getReasonPhrase());
            } else {
                e.getStatusCode();
            }
            String responseBody = new String(e.getResponseBodyAsByteArray(), StandardCharsets.UTF_8);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String absSingleResponse = objectMapper.readValue(responseBody, String.class);
                throw new GlobalExceptionMessage(absSingleResponse);
            } catch (JsonProcessingException ex) {
                throw new GlobalExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        throw new GlobalExceptionMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());


    }

    private HttpHeaders getBaseHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("yesvo", "123");
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.add("Authorization", "Bearer " + generateToken.getToken().getAccess_token());
//        headers.add("signature", "REDACTED");
//        headers.add("timestamp", "1754387820363");
//        headers.add("serialNumber", "REDACTED");
        return headers;
    }

    private String getUrl(String endpoint) {
        return String
                .format(
                        "%s/%s",
//                        "%s://%s:%s/%s",
//                        externalConfig.getProtocol(),
//                        externalConfig.getHost(),
//                        externalConfig.getPort(),
                          externalConfig.getBaseUrl(),
                        endpoint
                );
    }



}
