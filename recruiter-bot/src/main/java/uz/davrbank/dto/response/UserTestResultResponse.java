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
public class UserTestResultResponse {
    private Integer correctAnswers; // Number of correct answers
    private Integer totalQuestions; // Total questions in the test
    private double percentage;  // Score percentage
    private Integer totalPoints;
}
