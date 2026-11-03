package uz.davrbank.educontrolservice.aggregate.dto.rating;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import uz.davrbank.educontrolservice.aggregate.enums.RatingTargetType;

public record RatingRequestDTO(

        @NotNull(message = "Rating target type is required")
        @JsonProperty("rating_target_type")
        RatingTargetType ratingTargetType,

        @NotNull(message = "Target ID is required")
        @JsonProperty("target_id")
        Long targetId,

        @NotBlank(message = "User ID is required")
        @JsonProperty("user_id")
        String userId,

        @NotNull(message = "Rating is required")
        @DecimalMin(value = "0.0", message = "Rating must be at least 0")
        @DecimalMax(value = "5.0", message = "Rating cannot exceed 5")
        Double rating
) {
}
