package uz.davrbank.schotoborotservice.aggregate.config;

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

    private static final Logger log = LoggerFactory.getLogger(FeignErrorDecoder.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.resolve(response.status());
        String message = "Unknown error";

        try {
            if (response.body() != null) {
                String responseBody = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
                log.error("Feign responseBody: {}", responseBody);

                if (!responseBody.trim().startsWith("{")) {
                    message = responseBody;
                } else {
                    JsonNode root = mapper.readTree(responseBody);
                    String msg = root.has("message") ? root.get("message").asText() : "";
                    String meta = "";

                    if (root.has("metaData") && root.get("metaData").isArray()) {
                        meta = StreamSupport.stream(root.get("metaData").spliterator(), false)
                                .map(JsonNode::asText)
                                .collect(Collectors.joining("; "));
                    }

                    message = !meta.isEmpty() ? msg + " — " + meta : msg;
                }
            }
        } catch (IOException e) {
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
