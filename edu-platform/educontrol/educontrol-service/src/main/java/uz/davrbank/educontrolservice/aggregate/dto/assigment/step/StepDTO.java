package uz.davrbank.educontrolservice.aggregate.dto.assigment.step;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StepDTO(Long id, String name, @JsonProperty("course_id") Long courseId) {
}