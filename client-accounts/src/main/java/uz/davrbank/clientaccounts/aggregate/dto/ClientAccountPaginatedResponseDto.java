package uz.davrbank.clientaccounts.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({
        "totalPages", "last", "pageNumber", "pageSize", "numberOfElements",
        "first", "empty", "totalElements", "data", "status", "error"
})
public class ClientAccountPaginatedResponseDto {

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

    @JsonProperty("totalElements")
    private Integer totalElements;

    @JsonProperty("data")
    private List<ClientAccountDataDto> data;

    @JsonProperty("status")
    private String status;

    @JsonProperty("error")
    private String error;
}