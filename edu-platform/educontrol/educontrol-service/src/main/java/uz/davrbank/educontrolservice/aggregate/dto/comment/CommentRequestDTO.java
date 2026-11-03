package uz.davrbank.educontrolservice.aggregate.dto.comment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import uz.davrbank.educontrolservice.aggregate.enums.CommentTargetType;


public record CommentRequestDTO(

        @NotNull(message = "Target ID is required")
        @JsonProperty("target_id")
        Long targetId,

        @NotNull(message = "Target type is required")
        @JsonProperty("target_type")
        CommentTargetType targetType,

        @NotBlank(message = "User ID is required")
        @JsonProperty("user_id")
        String userId,

        @NotBlank(message = "Message is required")
        @Size(min = 1, max = 5000, message = "Message must be between 1 and 5000 characters")
        String message,

        @JsonProperty("parent_id")
        Long parentId // null if not a reply

) {
}
