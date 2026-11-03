package uz.davrbank.accounttocardservice.aggregate.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UzCardTransCreditRequestDto {

    private Integer id;
    private String jsonrpc;
    private String method;
    private Object params;

}
