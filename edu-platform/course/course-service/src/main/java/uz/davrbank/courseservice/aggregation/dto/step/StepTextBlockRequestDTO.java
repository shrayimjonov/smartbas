package uz.davrbank.courseservice.aggregation.dto.step;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StepTextBlockRequestDTO(

        Long stepId,
        String title,
        String body
) {
}
