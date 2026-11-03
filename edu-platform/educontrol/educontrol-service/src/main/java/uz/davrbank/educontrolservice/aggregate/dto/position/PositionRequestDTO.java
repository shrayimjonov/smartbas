package uz.davrbank.educontrolservice.aggregate.dto.position;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PositionRequestDTO(

        @NotBlank(message = "Position name is required")
        @Size(min = 2, max = 100, message = "Position name must be between 2 and 100 characters")
        String name
) {
}
