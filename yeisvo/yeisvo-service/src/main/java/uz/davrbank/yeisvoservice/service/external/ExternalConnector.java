package uz.davrbank.yeisvoservice.service.external;


import uz.davrbank.yeisvoservice.exception.controller.GlobalExceptionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ExternalConnector {


    private static final Logger logger = LogManager.getLogger(ExternalConnector.class);


    private final WebClient webClient;

    public ExternalConnector(WebClient webClient) {
        this.webClient = webClient;
    }


    public <REQ, RES> ResponseEntity<RES> exchangeWebClient(
            String endpoint,
            HttpMethod httpMethod,
            HttpHeaders httpHeaders,
            Duration timeout,
            REQ request,
            ParameterizedTypeReference<RES> responseType
    ) {
        try {
            return webClient
                    .method(httpMethod)
                    .uri(endpoint)
                    .headers(h -> h.addAll(httpHeaders))
                    .bodyValue(request)
                    .retrieve()
                    .toEntity(responseType)
                    .block(timeout);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new GlobalExceptionMessage(e.getMessage());
        }
    }

}

