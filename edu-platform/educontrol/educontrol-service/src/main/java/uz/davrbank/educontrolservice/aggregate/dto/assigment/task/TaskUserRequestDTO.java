package uz.davrbank.educontrolservice.aggregate.dto.assigment.task;

import com.fasterxml.jackson.annotation.JsonProperty;

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
