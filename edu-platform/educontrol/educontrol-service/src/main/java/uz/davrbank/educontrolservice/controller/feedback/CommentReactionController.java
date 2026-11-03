package uz.davrbank.educontrolservice.controller.feedback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.commentReaction.CommentReactionRequestDTO;
import uz.davrbank.educontrolservice.aggregate.enums.ReactionType;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.feedback.CommentReactionInterface;

@RestController
@RequestMapping("/api/v1/educontrol/feedback/reactions")
public class CommentReactionController {

    private final CommentReactionInterface commentReactionInterface;

    public CommentReactionController(CommentReactionInterface commentReactionInterface) {
        this.commentReactionInterface = commentReactionInterface;
    }


    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addReaction(@RequestBody CommentReactionRequestDTO commentReactionRequestDTO) {
        commentReactionInterface.addReaction(commentReactionRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "Реакция успешно добавлена", null));
    }

    @DeleteMapping("/remove/{reactionId}")
    public ResponseEntity<ApiResponse<Void>> removeReaction(@PathVariable Long reactionId) {
        commentReactionInterface.removeReaction(reactionId);
        return ResponseEntity.ok(ApiResponse.of("Реакция успешно удалена"));
    }

    @PutMapping("/update/{reactionId}")
    public ResponseEntity<ApiResponse<Void>> updateReaction(@PathVariable Long reactionId,
                                                            @RequestParam("reaction") ReactionType reactionType) {
        commentReactionInterface.updateReaction(reactionId, reactionType);
        return ResponseEntity.ok(ApiResponse.of("Реакция успешно обновлена"));
    }


    @GetMapping("/get/count")
    public ResponseEntity<ApiResponse<Object>> getReactionCount(@RequestParam Long commentId) {
        return ResponseEntity.ok(ApiResponse.of("Количество реакций получено",
                commentReactionInterface.getReaction(commentId)));
    }
}
