package uz.davrbank.courseservice.aggregation.dto.step;

public record AttachStepResponseDTO(
        String employeeId,
        Long stepId,
        String stepName,
        String employeeName,
        Boolean isCompleted,
        String status,
        String startedDate,
        String completedDate
        ) {
}