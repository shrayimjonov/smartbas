package uz.davrbank.courseservice.aggregation.dto.step;

public record StepTextBlockResponseDTO(

        Long id,
        Long stepId,
        String title,
        String body,
        Integer orderInStep
) {
}
