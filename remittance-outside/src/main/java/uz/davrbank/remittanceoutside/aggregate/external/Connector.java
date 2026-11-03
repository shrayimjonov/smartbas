/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.aggregate.external;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class Connector {

    private static final Logger LOG = LoggerFactory.getLogger(Connector.class);

    private final Map<String, WebClient> clients;

    public <REQUEST, RESPONSE> ResponseEntity<RESPONSE> exchange(
            String clientName,
            String path,
            HttpMethod method,
            Map<String, ?> params,
            HttpHeaders headers,
            Duration timeout,
            REQUEST body,
            ParameterizedTypeReference<RESPONSE> responseType) {

        LOG.info("Exchange parameters: {} : {} : {}: {}", clientName, path, method, params);

        WebClient webClient = clients.get(clientName);

        if (webClient == null) {
            LOG.info("Can't find client {}", clientName);
            throw new IllegalArgumentException("Client not found: " + clientName);
        }
        var request = webClient
                .method(method)
                .uri(uriBuilder -> {
                    uriBuilder.path(path);
                    if (params != null && !params.isEmpty()) {
                        for (Map.Entry<String, ?> param : params.entrySet()) {
                            uriBuilder.queryParam(param.getKey(), param.getValue());
                        }
                    }
                    LOG.info("Exchange parameters: {} : {}: {}", clientName, path, method);
                    return uriBuilder.build();
                }).headers(httpHeaders -> httpHeaders.addAll(headers));

        Mono<ResponseEntity<RESPONSE>> mono = (body != null ? request.bodyValue(body) : request)
                .retrieve()
                .toEntity(responseType)
                .timeout(timeout);

        return mono.block();
    }
}
