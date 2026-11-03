package uz.davrbank.educontrolservice.aggregate.dto.commentReaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import uz.davrbank.educontrolservice.aggregate.enums.ReactionType;

public record CommentReactionRequestDTO(

        @JsonProperty("comment_id")
        Long commentId,
        @JsonProperty("user_id")
        String userId,
        ReactionType reaction
) {
}
