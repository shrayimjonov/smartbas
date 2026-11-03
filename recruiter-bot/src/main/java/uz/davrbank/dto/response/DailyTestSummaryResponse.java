package uz.davrbank.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DailyTestSummaryResponse {
    private LocalDateTime date;
    private Long dailyTestTakers;
    private Long dailyPassedCount;
    private Long dailyFailedCount;
    private Long departmentCount;
    private Double testTakersPercentage;

}
