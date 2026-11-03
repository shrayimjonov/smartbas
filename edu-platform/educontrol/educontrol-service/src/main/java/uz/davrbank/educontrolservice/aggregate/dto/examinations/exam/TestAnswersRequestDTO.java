package uz.davrbank.educontrolservice.aggregate.dto.examinations.exam;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestAnswersRequestDTO {

    @JsonProperty("question_id")
    private Long questionId;

    @JsonProperty("answer_id")
    private Long answerId;

}
