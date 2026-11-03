package uz.davrbank.assignmentservice.aggregation.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public record TaskResponseDTO(

        String id,
        @JsonProperty("full_name")
        String fullName,
        @JsonProperty("task_name")
        String taskName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        String startDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        String endDate,
        String description,
        Double score,
        @JsonProperty("priority")
        String priority,
        String status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        @JsonProperty("completed_date")
        String completedDate,
        String expired_status
        ) {
}
