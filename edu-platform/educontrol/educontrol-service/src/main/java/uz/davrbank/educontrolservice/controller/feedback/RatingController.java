package uz.davrbank.educontrolservice.controller.feedback;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.davrbank.educontrolservice.aggregate.dto.rating.RatingRequestDTO;
import uz.davrbank.educontrolservice.exception.ApiResponse;
import uz.davrbank.educontrolservice.service.dao.feedback.RatingInterface;


@RestController
@RequestMapping("/api/v1/educontrol/feedback/ratings")
public class RatingController {
    private final RatingInterface ratingInterface;

    public RatingController(RatingInterface ratingInterface) {
        this.ratingInterface = ratingInterface;
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Void>> addRating(@RequestBody RatingRequestDTO requestDTO) {
        ratingInterface.addRating(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(201, "Рейтинг успешно добавлен", null));
    }

    @DeleteMapping("/remove/{ratingId}")
    public ResponseEntity<ApiResponse<Void>> removeRating(@PathVariable Long ratingId) {
        ratingInterface.removeRating(ratingId);
        return ResponseEntity.ok(ApiResponse.of("Рейтинг успешно удалён"));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Void>> updateRating(@RequestParam Long ratingId,
                                                          @RequestParam Double rating) {
        ratingInterface.updateRating(ratingId, rating);
        return ResponseEntity.ok(ApiResponse.of("Рейтинг успешно обновлён"));
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Object>> getRating(@RequestParam Long targetId,
                                                         @RequestParam String targetType) {
        return ResponseEntity.ok(ApiResponse.of("Рейтинг получен",
                ratingInterface.getRating(targetId, targetType)));
    }


}
