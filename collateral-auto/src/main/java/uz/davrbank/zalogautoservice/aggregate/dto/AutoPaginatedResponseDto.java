package uz.davrbank.zalogautoservice.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoPaginatedResponseDto {

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
    private List<AutoDataDto> data;

    @JsonProperty("status")
    private String status;

    @JsonProperty("totalElements")
    private Integer totalElements;

    @JsonProperty("error")
    private String error;
}