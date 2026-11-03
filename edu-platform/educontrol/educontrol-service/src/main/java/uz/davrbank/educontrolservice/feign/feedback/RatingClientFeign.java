package uz.davrbank.educontrolservice.feign.feedback;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.rating.RatingRequestDTO;
import uz.davrbank.educontrolservice.feign.config.FeedbackFeignServiceConfiguration;

@FeignClient(name = "feedback-service", contextId = "rating", configuration = FeedbackFeignServiceConfiguration.class)
public interface RatingClientFeign {

    @PostMapping("/api/v1/feedback/ratings/add")
    Object addRating(@RequestBody RatingRequestDTO requestDTO);

    @DeleteMapping("/api/v1/feedback/ratings/remove/{ratingId}")
    Object removeRating(@PathVariable Long ratingId,
                        @RequestParam String userId);

    @PutMapping("/api/v1/feedback/ratings/update")
    Object updateRating(@RequestParam Long ratingId,
                         @RequestParam String userId,
                         @RequestParam Double rating);

    @GetMapping("/api/v1/feedback/ratings/get")
    Object getRating(@RequestParam Long targetId,
                     @RequestParam String targetType);
}
