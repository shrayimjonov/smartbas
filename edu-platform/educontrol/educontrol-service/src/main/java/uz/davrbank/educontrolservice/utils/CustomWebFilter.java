package uz.davrbank.educontrolservice.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class CustomWebFilter implements WebFilter {
    private final static Logger log = LogManager.getLogger(CustomWebFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startTime = System.currentTimeMillis();

        // Access necessary information before calling chain.filter(exchange)
        HttpMethod method = exchange.getRequest().getMethod();
        String path = exchange.getRequest().getPath().value();

        // Store information in attributes for later retrieval if needed
        exchange.getAttributes().put("startTime", startTime);
        exchange.getAttributes().put("method", method);
        exchange.getAttributes().put("path", path);

        try {
            return chain
                    .filter(exchange)
                    .doFinally(signalType -> {
                        long totalTime = System.currentTimeMillis() - startTime;

                        // Retrieve information from attributes
                        HttpMethod storedMethod = exchange.getAttribute("method");
                        String storedPath = exchange.getAttribute("path");

                        StringBuilder logMessage = new StringBuilder();
                        logMessage.append("time: ").append(totalTime).append("ms").append("\t");
                        logMessage.append("method: ").append(storedMethod).append("\t");
                        logMessage.append("url: ").append(storedPath).append("\t");
                        logMessage.append("status: ").append(exchange.getResponse().getStatusCode()).append("\t");

                        // Handle the potential IllegalArgumentException here
                        try {
                            logMessage.append("remoteAddress: ").append(exchange.getRequest()
                                    .getRemoteAddress()).append("\t");
                        } catch (IllegalArgumentException e) {
                            log.warn("Error getting remote address: " + e.getMessage());
                        }

                        log.info(logMessage);
                    });
        } catch (Exception e) {
            log.error("Error in filter: " + e.getMessage());
            // You may choose to rethrow the exception or handle it in some way
            return Mono.error(e);
        }
    }
}