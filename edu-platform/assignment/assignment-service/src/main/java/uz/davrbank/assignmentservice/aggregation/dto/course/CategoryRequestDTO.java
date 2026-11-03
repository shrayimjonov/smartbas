package uz.davrbank.assignmentservice.aggregation.dto.course;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CategoryRequestDTO(

        String name,
        String description,
        @JsonProperty("type_id")
        Long typeId
) {
}
