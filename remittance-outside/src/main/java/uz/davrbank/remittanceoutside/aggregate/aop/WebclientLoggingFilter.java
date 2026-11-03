/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.aggregate.aop;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import java.util.*;

@Slf4j
public class WebclientLoggingFilter {

    public static ExchangeFilterFunction requestResponseLogging(ApiExchangeLogger apiExchangeLogger) {
        return (request, next) -> {
            long start = System.nanoTime();

            final String eventId = Optional.ofNullable(MDC.get("eventId"))
                    .orElseGet(() -> UUID.randomUUID().toString());

            ClientRequest newRequest = ClientRequest.from(request)
                    .build();

            String method = newRequest.method().name();
            String url = newRequest.url().toString();

            try {
                ApiExchangeLogger logger = apiExchangeLogger;
                Map<String, List<String>> headersMap = headersToMap(newRequest.headers());
                logger.logThirdPartyRequest(eventId, method, url, headersMap, null);
            } catch (Exception e) {
                log.error("Failed to log webclient request: {}", e.getMessage());
            }

            final String m = method;
            final String u = url;
            final long s = start;
            final ApiExchangeLogger logger = apiExchangeLogger;
            return next.exchange(newRequest)
                    .doOnSuccess(resp -> logResponse(resp, m, u, s, eventId, logger))
                    .doOnError(ex -> logError(ex, m, u, s, eventId, logger));
        };
    }

    private static void logResponse(ClientResponse resp, String method, String url, long start, String eventId, ApiExchangeLogger apiExchangeLogger) {
        long durMs = (System.nanoTime() - start) / 1_000_000;
        int status = resp.statusCode().value();
        try {
            Map<String, List<String>> headersMap = headersToMap(resp.headers().asHttpHeaders());
            apiExchangeLogger.logThirdPartyResponse(eventId, method, url, headersMap, null, String.valueOf(status), durMs);
        } catch (Exception e) {
            log.error("Failed to log webclient response: {}", e.getMessage());
        }
    }

    private static void logError(Throwable ex, String method, String url, long start, String eventId, ApiExchangeLogger apiExchangeLogger) {
        long durMs = (System.nanoTime() - start) / 1_000_000;
        try {
            apiExchangeLogger.logThirdPartyError(eventId, method, url, ex.getClass().getSimpleName() + ": " + Optional.ofNullable(ex.getMessage()).orElse(""), durMs);
        } catch (Exception e) {
            log.error("integration.error method={} url={} exception={} durationMs={}", method, url, ex.getClass().getSimpleName(), durMs);
        }
    }

    private static Map<String, List<String>> headersToMap(HttpHeaders headers) {
        Map<String, List<String>> map = new LinkedHashMap<>();
        headers.forEach((k, v) -> map.put(k, new ArrayList<>(v)));
        return map;
    }
}
