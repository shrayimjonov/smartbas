package uz.davrbank.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LastSixMonthsStatisticsResponse {
    private String month;                 // Month name for statistics
    private Long monthlyPassedCount;      // Count of passed tests for the month
    private Long monthlyFailedCount;      // Count of failed tests for the month
    private Long monthlyTestTakers;       // Count of test takers for the month
}
