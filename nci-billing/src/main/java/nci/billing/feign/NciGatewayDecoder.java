package nci.billing.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import nci.billing.exception.BadRequest;
import nci.billing.exception.ClosedLoan;
import nci.billing.exception.InternalError;
import nci.billing.exception.NotFound;
import nci.billing.model.dto.NciErrorDto;

import java.io.IOException;
import java.io.InputStream;

public class NciGatewayDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        NciErrorDto dto;
        try (InputStream bodyIs = response.body()
                .asInputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            dto = mapper.readValue(bodyIs, NciErrorDto.class);
        } catch (IOException e) {
            return new Exception(e.getMessage());
        }

        return switch (response.status()) {
            case 400 -> new BadRequest(dto.getError() != null ? dto.getError() : "Bad Request");
            case 403 -> new ClosedLoan(dto.getError() != null ? dto.getError() : "Loan is not found or closed");
            case 404 -> new NotFound(dto.getError() != null ? dto.getError() : "Not found");
            default -> new InternalError(dto.getError() != null ? dto.getError() : "Internal Server Error");
        };

//        return switch (response.status()) {
//            case 400 -> new BadRequest("Bad Request");
//            case 403 -> new ClosedLoan("Loan is not found or closed");
//            case 404 -> new NotFound("Not found");
//            default -> new InternalError("Internal Server Error");
//        };
    }
}
