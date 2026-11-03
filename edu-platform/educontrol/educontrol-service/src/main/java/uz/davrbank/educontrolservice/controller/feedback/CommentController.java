package uz.davrbank.educontrolservice.controller.feedback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.comment.CommentRequestDTO;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.feedback.CommentInterface;


@RestController
@RequestMapping("/api/v1/educontrol/feedback/comments")
public class CommentController {
    private final CommentInterface commentInterface;

    public CommentController(CommentInterface commentInterface) {
        this.commentInterface = commentInterface;
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Object>> createComment(@RequestBody CommentRequestDTO commentRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Комментарий успешно создан",
                        commentInterface.createComment(commentRequestDTO)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Object>> updateComment(@PathVariable Long id,
                                                             @RequestBody CommentRequestDTO commentRequestDTO) {
        return ResponseEntity.ok(ApiResponse.of("Комментарий успешно обновлён",
                commentInterface.updateComment(id, commentRequestDTO)));
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse<Object>> getAllComments(@RequestParam Long targetId,
                                                              @RequestParam String targetType) {
        return ResponseEntity.ok(ApiResponse.of("Список комментариев получен",
                commentInterface.getAllComments(targetId, targetType)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Object>> deleteComment(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.of("Комментарий успешно удалён",
                commentInterface.deleteComment(id)));
    }
}
