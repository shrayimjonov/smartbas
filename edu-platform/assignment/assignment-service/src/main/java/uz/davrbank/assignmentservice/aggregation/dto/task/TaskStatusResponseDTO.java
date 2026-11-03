package uz.davrbank.assignmentservice.aggregation.dto.task;


import java.util.List;
import java.util.Map;

public record TaskStatusResponseDTO(
        int incompleteCount,
        List<TaskResponseEmployeeDepartmentDTO> taskResponseDTO,
        Map<String, Integer> allStatusCounts

) {
}
