package uz.davrbank.courseservice.aggregation.dto.course;

public record UserCourseResponseDTO(
        String employeeId,
        Long courseId,
        String courseName,
        String employeeName,
        String status,
        String startedDate,
        String completedDate
) {
}
