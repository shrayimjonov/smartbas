package uz.davrbank.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Lob;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateTestRequest {

    @NotNull(message = "Department ID must not be null")
    private Long departmentId;

    @Valid
    @NotNull(message = "testQuestion must not be null")
    private QuestionTextDto testQuestion;

    @Valid
    @NotNull(message = "questionA must not be null")
    private QuestionTextDto questionA;

    @Valid
    @NotNull(message = "questionB must not be null")
    private QuestionTextDto questionB;

    @Valid
    @NotNull(message = "questionC must not be null")
    private QuestionTextDto questionC;

    @Valid
    @NotNull(message = "questionD must not be null")
    private QuestionTextDto questionD;

    @NotBlank(message = "correctAnswer must not be null")
    private String correctAnswer;

    @Lob
    private byte[] photo;


}

