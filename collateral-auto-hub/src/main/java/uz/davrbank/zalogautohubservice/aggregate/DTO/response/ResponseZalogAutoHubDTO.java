package uz.davrbank.zalogautohubservice.aggregate.DTO.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseZalogAutoHubDTO implements Serializable {

    @JsonProperty("status")
    private String status;
    @JsonProperty("totalElements")
    private Integer totalElements;
    @JsonProperty("error")
    private ResponseError error;
    @JsonProperty("data")
    private List<ResponseDataDTO> data;
}