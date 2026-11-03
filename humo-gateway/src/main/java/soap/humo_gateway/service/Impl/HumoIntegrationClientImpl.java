package soap.humo_gateway.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.stereotype.Component;
import soap.humo_gateway.config.JsonWebClient;
import soap.humo_gateway.dto.property.HumoIntegrationProperty;
import soap.humo_gateway.dto.response.humoIntegration.BinResponse;
import soap.humo_gateway.exception.ConnectionError;
import soap.humo_gateway.service.HumoIntegrationClientService;

import java.net.URI;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class HumoIntegrationClientImpl implements HumoIntegrationClientService {
    private final JsonWebClient webClient;
    private final HumoIntegrationProperty property;

    @Override
    public <B, R> R sendPostRequest(B body, Class<R> clazz, String pathURI) {
        log.info("Starting internal request to: {}", property.getUrl());
        try {
            var response = webClient.internalClient()
                    .post()
                    .uri(URI.create(pathURI))
                    .bodyValue(body)
                    .exchangeToMono(res -> res.bodyToMono(clazz))
                    .block();
            log.debug("humo integration response: {}", response);
            return Objects.requireNonNull(response);
        } catch (Exception ex) {
            log.error("humo integration error: {}", ex.getMessage());
            throw new ConnectionError(ex.getMessage());
        }
    }

    @Override
    public <R> R sendGetRequest(Class<R> clazz, String pathURI) {
        log.info("Starting internal request to: {}", property.getUrl());
        try {
            var response = webClient.internalClient()
                    .get()
                    .uri(URI.create(pathURI))
                    .exchangeToMono(res -> res.bodyToMono(clazz))
                    .block();
            log.debug("humo integration response: {}", response);
            return Objects.requireNonNull(response);
        } catch (Exception ex) {
            log.error("humo integration error: {}", ex.getMessage());
            throw new ConnectionError(ex.getMessage());
        }
    }
}
