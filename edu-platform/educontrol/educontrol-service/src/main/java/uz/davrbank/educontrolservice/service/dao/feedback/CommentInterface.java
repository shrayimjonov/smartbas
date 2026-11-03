package uz.davrbank.educontrolservice.service.dao.feedback;

import uz.davrbank.educontrolservice.aggregate.dto.comment.CommentRequestDTO;

import java.util.List;

public interface CommentInterface {

    Object createComment(CommentRequestDTO commentRequestDTO);

    Object updateComment(Long commentId, CommentRequestDTO commentRequestDTO);

    List<Object> getAllComments(Long targetId, String targetType);

    Object deleteComment(Long commentId);
}
