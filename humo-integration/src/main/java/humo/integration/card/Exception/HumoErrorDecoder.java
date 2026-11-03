package humo.integration.card.Exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import humo.integration.card.Model.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@Slf4j
public class HumoErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {


        ExceptionMessage message;
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            message = mapper.readValue(bodyIs, ExceptionMessage.class);
            log.error("Exception body is: {}", message);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }

        log.warn("Exception status is: {}", response.status());

        switch (response.status()) {
            case 400:
                return new ThirdPartyException(message.getError().getMessage() != null ? message.getError().getMessage() : "Bad Request");
            case 403:
                return new CardHasNotAccessException(message.getError().getMessage()!=null ? message.getError().getMessage() : "Card has not access");
            case 404:
                return new NotFoundException(message.getError().getMessage() != null ? message.getError().getMessage() : "Not found");
            case 500:
                return new CardNotFoundException(message.getError().getMessage() != null ? message.getError().getMessage() : "Card not found from huMo");

            default:
                return errorDecoder.decode(methodKey, response);
        }
    }

}
