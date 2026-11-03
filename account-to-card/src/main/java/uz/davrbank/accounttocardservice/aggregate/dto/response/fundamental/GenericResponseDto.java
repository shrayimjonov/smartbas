package uz.davrbank.accounttocardservice.aggregate.dto.response.fundamental;

import lombok.Data;
import uz.davrbank.accounttocardservice.exception.controller.ErrorResponse;

@Data
public class GenericResponseDto <T> {
    private String jsonrpc = "2.0";
    private Integer id;
    private T result;
    private ErrorResponse error;
}