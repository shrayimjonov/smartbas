package uz.davrbank.educontrolservice.aggregate.dto.assigment.step;

public record StepTextBlockRequestDTO(

        Long stepId,
        String title,
        String body
) {
}
