package uz.davrbank.educontrolservice.aggregate.dto.examinations.question;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AnswerDTO {
    private Long id;
    private String answer;
    private Boolean correct;
}
