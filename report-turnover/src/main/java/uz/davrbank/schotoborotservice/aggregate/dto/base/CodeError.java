package uz.davrbank.schotoborotservice.aggregate.dto.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 15.12.2025
 * Time: 13:29
 * Project: schotoborot-service
 */
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