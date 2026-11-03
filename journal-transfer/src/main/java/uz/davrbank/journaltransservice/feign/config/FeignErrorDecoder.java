package uz.davrbank.journaltransservice.feign.config;

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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FeignErrorDecoder implements ErrorDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(FeignErrorDecoder.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {

        HttpStatus status = HttpStatus.resolve(response.status());
        String message = "Unknown error";

        try {
            if (response.body() != null) {

                String responseBody = new String(
                        response.body().asInputStream().readAllBytes(),
                        StandardCharsets.UTF_8
                );

                LOG.error("Feign responseBody: {}", responseBody);

                if (!responseBody.trim().startsWith("{")) {
                    message = responseBody; // plain text error
                } else {
                    JsonNode root = MAPPER.readTree(responseBody);

                    String msg = root.has("message") ? root.get("message").asText() : "";
                    String meta = "";

                    if (root.has("metadata") && root.get("metadata").isArray()) {
                        meta = StreamSupport.stream(root.get("metadata").spliterator(), false)
                                .map(JsonNode::asText)
                                .collect(Collectors.joining("; "));
                    }

                    message = !meta.isEmpty() ? msg + " - " + meta : msg;
                }
            }

        } catch (IOException e) {
            LOG.error("Failed to decode error response", e);
            message = "Failed to read error response: " + e.getMessage();
        }

        if (response.status() == 503) {
            message = "Hub service unavailable. Please try again later.";
        }

        return new ResponseStatusException(
                status != null ? status : HttpStatus.INTERNAL_SERVER_ERROR,
                message
        );
    }
}
