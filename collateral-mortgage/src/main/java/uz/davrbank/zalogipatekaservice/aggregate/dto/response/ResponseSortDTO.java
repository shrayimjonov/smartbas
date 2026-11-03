package uz.davrbank.zalogipatekaservice.aggregate.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseSortDTO extends ResponseCoreDTO implements Serializable {

    @JsonProperty("totalPages")
    private Integer totalPages;
    @JsonProperty("last")
    private Boolean last;
    @JsonProperty("pageNumber")
    private Integer pageNumber;
    @JsonProperty("pageSize")
    private Integer pageSize;
    @JsonProperty("numberOfElements")
    private Integer numberOfElements;
    @JsonProperty("first")
    private Boolean first;
    @JsonProperty("empty")
    private Boolean empty;
    @JsonProperty("data")
    private List<ResponseDataDTO> data;
}
