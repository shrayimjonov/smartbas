package uz.davrbank.educontrolservice.aggregate.dto.examinations.exam;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExamFilterRequestDTO {

    @JsonProperty("courseId")
    private Long courseId;

    @JsonProperty("cloudresourceId")
    private Long cloudresourceId;

    @JsonProperty("type")
    private TypeGeneral type;

}
