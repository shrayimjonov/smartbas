package uz.davrbank.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAnswerRequestDto {
    @NotNull(message = "questionId must not be null")
    private Long questionId; // SurveyForm question ID
    @NotNull(message = "selectedAnswer must not be null")
    private String selectedAnswer; // User's selected answer (e.g., A, B, C, D)


}
