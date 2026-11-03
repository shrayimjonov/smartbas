package uz.davrbank.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Lob;
import lombok.*;
import uz.davrbank.entity.QuestionText;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateTestResponse {
    private Long id;
    private Long departmentId;
    private QuestionText testQuestion;
    private QuestionText questionA;
    private QuestionText questionB;
    private QuestionText questionC;
    private QuestionText questionD;
    private String correctAnswer;
    @Lob
    private byte[] photo;
}
