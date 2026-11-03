package uz.davrbank.zalogautoservice.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseError implements Serializable {
    @JsonProperty("status")
    private String status;

    @JsonProperty("error")
    private String error;

    @JsonProperty("totalElements")
    private Integer totalElements;

    @JsonProperty("data")
    private AutoResponseDto data;
}
