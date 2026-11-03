package uz.davrbank.payment.model.dto.uzcard.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.nodir.commondto.model.dto.core.response.CoreError;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UzCardBaseResponse <R> {
    @JsonProperty("jsonrpc")
    private String jsonRPC;
    @JsonProperty("id")
    private String id;
    @JsonProperty("result")
    private R result;
    @JsonProperty("error")
    private CoreError error;
}
