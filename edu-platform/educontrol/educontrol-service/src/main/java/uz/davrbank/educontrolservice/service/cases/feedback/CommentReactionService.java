package uz.davrbank.educontrolservice.service.cases.feedback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.commentReaction.CommentReactionRequestDTO;
import uz.davrbank.educontrolservice.aggregate.enums.ReactionType;
import uz.davrbank.educontrolservice.feign.feedback.CommentReactionClientFeign;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;
import uz.davrbank.educontrolservice.service.dao.feedback.CommentReactionInterface;

@Slf4j
@Service
public class CommentReactionService implements CommentReactionInterface {
    private final CommentReactionClientFeign commentReactionClientFeign;
    private final JwtTokenService jwtTokenService;

    public CommentReactionService(CommentReactionClientFeign commentReactionClientFeign, JwtTokenService jwtTokenService) {
        this.commentReactionClientFeign = commentReactionClientFeign;
        this.jwtTokenService = jwtTokenService;
    }


    @Override
    public void addReaction(CommentReactionRequestDTO commentReactionRequestDTO) {
        log.info("REQUEST (addReaction) FROM [feedback-service]");
        String userId = jwtTokenService.getCurrentUserId();
        CommentReactionRequestDTO requestDTO = new CommentReactionRequestDTO(
                commentReactionRequestDTO.commentId(),
                userId,
                commentReactionRequestDTO.reaction()
        );
        commentReactionClientFeign.addReaction(requestDTO);
    }

    @Override
    public void removeReaction(Long reactionId) {
        log.info("REQUEST (removeReaction) FROM [feedback-service]");
        String userId = jwtTokenService.getCurrentUserId();
        commentReactionClientFeign.deleteReaction(reactionId, userId);
    }

    @Override
    public void updateReaction(Long reactionId, ReactionType reactionType) {
        log.info("REQUEST (updateReaction) FROM [feedback-service]");
        String userId = jwtTokenService.getCurrentUserId();
        commentReactionClientFeign.updateReaction(reactionId, userId, reactionType);
    }

    @Override
    public Object getReaction(Long commentId) {
        log.info("REQUEST (getReaction) FROM [feedback-service]");
        return commentReactionClientFeign.getReactionCount(commentId);
    }
}
