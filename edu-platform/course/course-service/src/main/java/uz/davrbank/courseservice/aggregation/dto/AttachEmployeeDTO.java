package uz.davrbank.courseservice.aggregation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AttachEmployeeDTO(
        @JsonProperty("employee_id") String employeeId,
        @JsonProperty("course_id") Long courseId) {
}
