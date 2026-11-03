package uz.davrbank.educontrolservice.aggregate.dto.library;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

public record LibraryRequestDTO(

        @NotBlank(message = "Title is required")
        @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
        String title,

        @Size(max = 2000, message = "Description cannot exceed 2000 characters")
        String description,

        @NotBlank(message = "Author is required")
        @Size(min = 1, max = 255, message = "Author name must be between 1 and 255 characters")
        String author,

        @NotNull(message = "Year is required")
        @Min(value = 1000, message = "Year must be at least 1000")
        @Max(value = 2100, message = "Year cannot exceed 2100")
        Integer year,

        @NotNull(message = "Category ID is required")
        @JsonProperty("category_id")
        Long categoryId,

        @Size(max = 50, message = "Language cannot exceed 50 characters")
        String language,

        @NotNull(message = "Department ID is required")
        @JsonProperty("department_id")
        Long departmentId
) {
}