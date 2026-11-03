package uz.davrbank.educontrolservice.feign.feedback;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.commentReaction.CommentReactionRequestDTO;
import uz.davrbank.educontrolservice.aggregate.enums.ReactionType;
import uz.davrbank.educontrolservice.feign.config.FeedbackFeignServiceConfiguration;

@FeignClient(name = "feedback-service", contextId = "reaction", configuration = FeedbackFeignServiceConfiguration.class)
public interface CommentReactionClientFeign {

    @PostMapping("/api/v1/feedback/reactions/add")
    Object addReaction(@RequestBody CommentReactionRequestDTO commentReactionRequestDTO);

    @DeleteMapping("/api/v1/feedback/reactions/remove/{reactionId}")
    Object deleteReaction(@PathVariable Long reactionId,
                          @RequestParam String userId);

    @PutMapping("/api/v1/feedback/reactions/update/{reactionId}")
    Object updateReaction(@PathVariable Long reactionId,
                          @RequestParam String userId,
                          @RequestParam("reaction") ReactionType reactionType);

    @GetMapping("/api/v1/feedback/reactions/get/count")
    Object getReactionCount(@RequestParam Long commentId);

}
