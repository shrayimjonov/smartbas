package uz.davrbank.courseservice.aggregation.dto.course;

public record CourseResponseDTO(
        Long id,
        String name,
        int timeComplete
) {
}
