package uz.davrbank.clientaccounts.feign.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class FeignErrorDecoder implements ErrorDecoder {
    private static final Logger log = LoggerFactory.getLogger(FeignErrorDecoder.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.resolve(response.status());
        String message = "Unknown error";

        try {
            if (response.body() != null) {
                String body = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
                log.error("Feign error body: {}", body);

                JsonNode root = mapper.readTree(body);
                String msg = root.has("error") ? root.get("error").asText() : "Hub service error";
                message = msg.isEmpty() ? "Hub returned error" : msg;
            }
        } catch (IOException e) {
            message = "Failed to parse error response";
        }

        if (response.status() == 503) {
            message = "Hub service temporarily unavailable";
        }

        return new ResponseStatusException(status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}