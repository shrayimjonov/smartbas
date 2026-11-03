package uz.davrbank.educontrolservice.service.dao.feedback;

import uz.davrbank.educontrolservice.aggregate.dto.commentReaction.CommentReactionRequestDTO;
import uz.davrbank.educontrolservice.aggregate.enums.ReactionType;

public interface CommentReactionInterface {

    void addReaction(CommentReactionRequestDTO commentReactionRequestDTO);

    void removeReaction(Long reactionId);

    void updateReaction(Long reactionId, ReactionType reactionType);

    Object getReaction(Long commentId);
}
