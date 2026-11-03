package uz.davrbank.zalogipatekaservice.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class ResponseCoreDTO  implements Serializable {
    @JsonProperty("status")
    private String status;
    @JsonProperty("totalElements")
    private Integer totalElements;
    @JsonProperty("error")
    private String error;
}