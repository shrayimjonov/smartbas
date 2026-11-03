package uz.davrbank.bustreasury.aggregate.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaymentRequestDTO {
    @JsonProperty("code")
    private String code;
    @JsonProperty("name")
    private String name;
}
