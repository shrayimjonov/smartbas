package uz.davrbank.educontrolservice.aggregate.dto.examinations.question;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("question")
    private String question;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficulty;

    @JsonProperty("examination_id")
    private Long examinationId;

    @JsonProperty("answers")
    private List<AnswerDTO> answers;
}