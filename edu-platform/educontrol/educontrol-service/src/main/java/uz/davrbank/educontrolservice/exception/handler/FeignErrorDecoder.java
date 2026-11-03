package uz.davrbank.educontrolservice.exception.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import uz.davrbank.educontrolservice.exception.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Custom Feign ErrorDecoder that properly handles errors from downstream services.
 * 
 * <p>This decoder:
 * <ul>
 *   <li>Parses error responses from downstream services</li>
 *   <li>Converts them to appropriate local exceptions</li>
 *   <li>Preserves error details for proper error propagation</li>
 *   <li>Handles both new ApiErrorResponse and legacy ErrorResponse formats</li>
 * </ul>
 * 
 * @version 1.0.0
 */
@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.resolve(response.status());
        String serviceName = extractServiceName(response.request().url());
        String responseBody = extractResponseBody(response);

        log.debug("Feign error: method={}, status={}, service={}, body={}",
                methodKey, response.status(), serviceName, responseBody);

        // Handle specific HTTP status codes
        return switch (status) {
            case UNAUTHORIZED -> handleUnauthorized(serviceName, responseBody);
            case FORBIDDEN -> handleForbidden(serviceName, responseBody);
            case NOT_FOUND -> handleNotFound(serviceName, responseBody);
            case CONFLICT -> handleConflict(serviceName, responseBody);
            case UNPROCESSABLE_ENTITY -> handleUnprocessableEntity(serviceName, responseBody);
            case BAD_REQUEST -> handleBadRequest(serviceName, responseBody);
            case SERVICE_UNAVAILABLE, BAD_GATEWAY, GATEWAY_TIMEOUT -> 
                    handleServiceUnavailable(serviceName, responseBody, status);
            default -> handleDefault(serviceName, responseBody, status);
        };
    }

    private Exception handleUnauthorized(String serviceName, String responseBody) {
        String message = extractMessage(responseBody, "Unauthorized access to " + serviceName);
        log.warn("Unauthorized access to downstream service: {}", serviceName);
        return new Exception(message);
    }

    private Exception handleForbidden(String serviceName, String responseBody) {
        String message = extractMessage(responseBody, "Access denied to " + serviceName);
        log.warn("Access denied to downstream service: {}", serviceName);
        return new Exception(message);
    }

    private Exception handleNotFound(String serviceName, String responseBody) {
        String message = extractMessage(responseBody, "Resource not found in " + serviceName);
        return new Exception(message);
    }

    private Exception handleConflict(String serviceName, String responseBody) {
        String message = extractMessage(responseBody, "Duplicate resource in " + serviceName);
        return new Exception(message);
    }

    private Exception handleUnprocessableEntity(String serviceName, String responseBody) {
        String message = extractMessage(responseBody, "Business validation failed in " + serviceName);
        return new Exception(message);
    }

    private Exception handleBadRequest(String serviceName, String responseBody) {
        String message = extractMessage(responseBody, "Bad request to " + serviceName);
        return new Exception(message);
    }

    private Exception handleServiceUnavailable(String serviceName, String responseBody, HttpStatus status) {
        String message = String.format("Service %s is unavailable (status: %d)", serviceName, status.value());
        log.error("Downstream service unavailable: {} - {}", serviceName, responseBody);
        return new Exception(message);
    }

    private Exception handleDefault(String serviceName, String responseBody, HttpStatus status) {
        String message = extractMessage(responseBody, 
                String.format("Error from %s (status: %d)", serviceName, status.value()));
        
        if (status.is4xxClientError()) {
            log.warn("Client error from downstream service {}: {}", serviceName, message);
            return new Exception(message);
        } else {
            log.error("Server error from downstream service {}: {}", serviceName, message);
            return new Exception(message);
        }
    }

    private String extractResponseBody(Response response) {
        if (response.body() == null) {
            return "";
        }
        try (InputStream inputStream = response.body().asInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.warn("Failed to read response body", e);
            return "";
        }
    }

    private String extractMessage(String responseBody, String defaultMessage) {
        if (responseBody == null || responseBody.isBlank()) {
            return defaultMessage;
        }
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            // Try ApiErrorResponse format
            if (root.has("message") && !root.get("message").isNull()) {
                return root.get("message").asText();
            }
            // Try legacy format
            if (root.has("description") && !root.get("description").isNull()) {
                return root.get("description").asText();
            }
            return defaultMessage;
        } catch (Exception e) {
            log.debug("Failed to parse error response: {}", e.getMessage());
            return defaultMessage;
        }
    }

    private String extractServiceName(String url) {
        if (url == null) return "unknown-service";
        try {
            // Extract service name from URL like http://service-name/api/...
            String[] parts = url.replace("http://", "").replace("https://", "").split("/");
            return parts.length > 0 ? parts[0].split(":")[0] : "unknown-service";
        } catch (Exception e) {
            return "unknown-service";
        }
    }
}

