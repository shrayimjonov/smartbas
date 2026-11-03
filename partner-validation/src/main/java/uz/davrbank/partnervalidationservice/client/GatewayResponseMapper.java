package uz.davrbank.partnervalidationservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import uz.davrbank.partnervalidationservice.exception.GatewayCommunicationException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GatewayResponseMapper {

    ObjectMapper objectMapper;

    public <T> T read(String rawResponse, Class<T> responseType) {
        return read(rawResponse, objectMapper.constructType(responseType));
    }

    public <T> T read(String rawResponse, ParameterizedTypeReference<T> responseType) {
        return read(rawResponse, objectMapper.constructType(responseType.getType()));
    }

    private <T> T read(String rawResponse, JavaType responseType) {
        if (rawResponse == null || rawResponse.isBlank()) {
            return null;
        }

        try {
            JsonNode rootNode = objectMapper.readTree(rawResponse);
            JsonNode normalizedNode = normalize(rootNode, responseType);
            if (normalizedNode == null || normalizedNode.isNull()) {
                return null;
            }
            return objectMapper.readerFor(responseType).readValue(normalizedNode);
        } catch (IOException exception) {
            throw new GatewayCommunicationException("Failed to decode gateway response", exception);
        }
    }

    private JsonNode normalize(JsonNode rootNode, JavaType responseType) {
        if (rootNode == null || rootNode.isNull() || rootNode.isMissingNode()) {
            return NullNode.instance;
        }
        if (rootNode.isArray() && !responseType.isContainerType()) {
            return rootNode.isEmpty() ? NullNode.instance : rootNode.get(0);
        }
        return rootNode;
    }
}
