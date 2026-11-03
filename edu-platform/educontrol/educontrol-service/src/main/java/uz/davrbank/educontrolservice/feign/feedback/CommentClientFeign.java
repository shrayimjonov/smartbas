package uz.davrbank.educontrolservice.feign.feedback;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.comment.CommentRequestDTO;
import uz.davrbank.educontrolservice.feign.config.BankMaterialsFeignServiceConfiguration;
import uz.davrbank.educontrolservice.feign.config.FeedbackFeignServiceConfiguration;

import java.util.List;

@FeignClient(name = "feedback-service", contextId = "comment", configuration = FeedbackFeignServiceConfiguration.class)
public interface CommentClientFeign {

    @PostMapping("/api/v1/feedback/comments/create")
    Object createComment(@RequestBody CommentRequestDTO commentRequestDTO);

    @PutMapping("/api/v1/feedback/comments/update/{id}")
    Object updateComment(@PathVariable Long id,
                         @RequestBody CommentRequestDTO commentRequestDTO);

    @GetMapping("/api/v1/feedback/comments/get/all")
    List<Object> getAllComments(@RequestParam Long targetId,
                                @RequestParam String targetType);

    @DeleteMapping("/api/v1/feedback/comments/delete/{id}")
    Object deleteComment(@PathVariable Long id,
                         @RequestParam String userId);


}
