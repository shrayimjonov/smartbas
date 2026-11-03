package uz.davrbank.clientaccounts.aggregate.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceCodeResponseDto<T> {

    @JsonProperty("status")
    private String status;

    @JsonProperty("error")
    private String error;

    @JsonProperty("totalElements")
    private Integer totalElements;

    @JsonProperty("data")
    private List<T> data;
}