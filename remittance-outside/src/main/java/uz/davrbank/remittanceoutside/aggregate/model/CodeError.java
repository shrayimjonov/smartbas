package uz.davrbank.remittanceoutside.aggregate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeError {
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;

    public CodeError(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}