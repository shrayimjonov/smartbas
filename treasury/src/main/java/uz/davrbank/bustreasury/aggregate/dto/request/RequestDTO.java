package uz.davrbank.bustreasury.aggregate.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestDTO {
    @JsonProperty("P_SUMMA")
    private long sum;
    @JsonProperty("P_DATE")
    private LocalDate date = LocalDate.now();
    @JsonProperty("crr_rates")
    private LocalDate crrRates;
}
