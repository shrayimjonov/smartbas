package uz.davrbank.courseservice.aggregation.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.davrbank.courseservice.aggregation.dto.step.StepRequestDTO;

import java.util.Set;

public record CourseRequestDTO(

        String name,

        @JsonProperty("time_complete")
        int timeComplete,

        @JsonProperty("category_id")
        Long categoryId,

        @JsonProperty("steps")
        Set<StepRequestDTO> steps,

        Double score,

        @JsonProperty("is_public")
        Boolean isPublic
) {
}