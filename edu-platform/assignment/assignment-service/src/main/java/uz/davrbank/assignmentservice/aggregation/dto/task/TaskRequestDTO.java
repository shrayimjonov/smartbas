package uz.davrbank.assignmentservice.aggregation.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TaskRequestDTO(

        @JsonProperty("name")
//        @NotBlank(message = "Name не должно быть пустым")
        String name,

        @JsonProperty("start_date")
//        @NotBlank(message = "Start Date не должно быть пустым")
        String startDate,

        @JsonProperty("description")
//        @NotBlank(message = "Description не должно быть пустым")
        String description,

        @JsonProperty("score")
//        @NotNull(message = "Score не должно быть пустым")
        Double score,

        @JsonProperty("end_date")
//        @NotBlank(message = "End Date не должно быть пустым")
        String endDate,


        @JsonProperty("employee_id")
        List<String> employeeId

) {
}
