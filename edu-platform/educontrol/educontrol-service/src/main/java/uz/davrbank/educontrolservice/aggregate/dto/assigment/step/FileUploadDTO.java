package uz.davrbank.educontrolservice.aggregate.dto.assigment.step;

public record FileUploadDTO(
        Long id,
        String name,
        String extension,
        String size,
        String type,
        Long stepId,
        String description,
        Long length,
        Double duration) {
}
