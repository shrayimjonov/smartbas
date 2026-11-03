package uz.davrbank.assignmentservice.aggregation.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TaskUserRequestDTO(

        @JsonProperty("name")
//        @NotBlank(message = "Name не должно быть пустым")
        String name,

        @JsonProperty("description")
//        @NotBlank(message = "Description не должно быть пустым")
        String description,

        @JsonProperty("end_date")
//        @NotBlank(message = "End Date не должно быть пустым")
        String endDate,

        @JsonProperty("priority")
//        @NotBlank(message = "Priority не должно быть пустым")
        String priority

) {
}
