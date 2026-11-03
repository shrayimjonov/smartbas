package uz.davrbank.educontrolservice.aggregate.dto.examinations.question;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionUpdateRequestDTO {
    @JsonProperty("question")
    @NotNull(message = "question text is required")
    private String question;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "question type is required")
    private QuestionType type;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "difficulty level is required")
    private DifficultyLevel difficulty;

    @JsonProperty("examination_id")
    @NotNull(message = "examination id is required")
    private Long examinationId;

    @JsonProperty("answers")
    @NotEmpty(message = "answers is required")
    private List<AnswerDTO> answers;
}
