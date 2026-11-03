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
public class TestSummaryResponse {
    private Long numberOfTestTakers;
    private Long passedCount;
    private Long failedCount;
    private Long departmentCount;
}
