package uz.davrbank.educontrolservice.service.cases.feedback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.comment.CommentRequestDTO;
import uz.davrbank.educontrolservice.feign.feedback.CommentClientFeign;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;
import uz.davrbank.educontrolservice.service.dao.feedback.CommentInterface;

import java.util.List;

@Service
@Slf4j
public class CommentService implements CommentInterface {
    private final CommentClientFeign commentClientFeign;
    private final JwtTokenService jwtTokenService;

    public CommentService(CommentClientFeign commentClientFeign, JwtTokenService jwtTokenService) {
        this.commentClientFeign = commentClientFeign;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Object createComment(CommentRequestDTO commentRequestDTO) {
        log.info("REQUEST (createComment) FROM [feedback-service]");
        String userId = jwtTokenService.getCurrentUserId();
        CommentRequestDTO requestDTO = new CommentRequestDTO(
                commentRequestDTO.targetId(),
                commentRequestDTO.targetType(),
                userId,
                commentRequestDTO.message(),
                commentRequestDTO.parentId()
        );
        return commentClientFeign.createComment(requestDTO);
    }

    @Override
    public Object updateComment(Long commentId, CommentRequestDTO commentRequestDTO) {
        log.info("REQUEST (updateComment) FROM [feedback-service]");
        String userId = jwtTokenService.getCurrentUserId();
        CommentRequestDTO requestDTO = new CommentRequestDTO(
                null,
                null,
                userId,
                commentRequestDTO.message(),
                null
        );
        return commentClientFeign.updateComment(commentId, requestDTO);
    }

    @Override
    public List<Object> getAllComments(Long targetId, String targetType) {
        log.info("REQUEST (getAllComments) FROM [feedback-service]");
        return commentClientFeign.getAllComments(targetId, targetType);
    }

    @Override
    public Object deleteComment(Long commentId) {
        log.info("REQUEST (deleteComment) FROM [feedback-service]");
        String userId = jwtTokenService.getCurrentUserId();
        return commentClientFeign.deleteComment(commentId, userId);
    }
}
