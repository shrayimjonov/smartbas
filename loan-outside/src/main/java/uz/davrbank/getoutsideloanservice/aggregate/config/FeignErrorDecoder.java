package uz.davrbank.getoutsideloanservice.aggregate.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FeignErrorDecoder implements ErrorDecoder {

    private final ObjectMapper objectMapper;

    public FeignErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.resolve(response.status());
        String message = "Unknown error";

        try {
            if (response.body() != null) {
                String responseBody = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
                message = extractMessage(responseBody);
            }
        } catch (IOException e) {
            message = "Failed to read error response: " + e.getMessage();
        }

        if (response.status() == HttpStatus.SERVICE_UNAVAILABLE.value()) {
            message = "Hub service unavailable. Please try again later.";
        }

        return new ResponseStatusException(
                status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR,
                message
        );
    }

    private String extractMessage(String responseBody) throws IOException {
        if (responseBody == null || responseBody.isBlank()) {
            return "Unknown error";
        }
        if (!responseBody.trim().startsWith("{")) {
            return responseBody;
        }

        JsonNode root = objectMapper.readTree(responseBody);
        if (root.hasNonNull("message")) {
            return root.get("message").asText();
        }
        if (root.hasNonNull("error")) {
            return root.get("error").asText();
        }
        return responseBody;
    }
}
