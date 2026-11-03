package humo.integration.card.Exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import humo.integration.card.Model.CardExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class HumoCardErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();


    @Override
    public Exception decode(String methodKey, Response response) {

        CardExceptionMessage message;
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, CardExceptionMessage.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }

        switch (response.status()) {
            case 400:
                return new CardNotFoundException(message.getError() != null ? message.getError().getMessage() : "Bad Request");
            case 404:
                return new NotFoundException(message.getError() != null ? message.getError().getMessage() : "Not found");
            case 500:
                return new CardNotFoundException(message.getError() != null ? message.getError().getMessage() : "Error from HUMO");
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }

}
