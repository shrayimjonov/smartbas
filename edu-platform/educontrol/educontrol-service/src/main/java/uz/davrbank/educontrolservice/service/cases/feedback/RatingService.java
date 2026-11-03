package uz.davrbank.educontrolservice.service.cases.feedback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.educontrolservice.aggregate.dto.rating.RatingRequestDTO;
import uz.davrbank.educontrolservice.feign.feedback.RatingClientFeign;
import uz.davrbank.educontrolservice.service.cases.jwt.JwtTokenService;
import uz.davrbank.educontrolservice.service.dao.feedback.RatingInterface;

@Service
@Slf4j
public class RatingService implements RatingInterface {
    private final RatingClientFeign ratingClientFeign;
    private final JwtTokenService jwtTokenService;

    public RatingService(RatingClientFeign ratingClientFeign, JwtTokenService jwtTokenService) {
        this.ratingClientFeign = ratingClientFeign;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public Object addRating(RatingRequestDTO requestDTO) {
        log.info("REQUEST (addRating) FROM [feedback-service]");
        String userId = jwtTokenService.getCurrentUserId();
        RatingRequestDTO ratingRequestDTO = new RatingRequestDTO(
                requestDTO.ratingTargetType(),
                requestDTO.targetId(),
                userId,
                requestDTO.rating()
        );
        return ratingClientFeign.addRating(ratingRequestDTO);
    }

    @Override
    public Object removeRating(Long ratingId) {
        log.info("REQUEST (removeRating) FROM [feedback-service]");
        String userId = jwtTokenService.getCurrentUserId();
        return ratingClientFeign.removeRating(ratingId, userId);
    }

    @Override
    public Object updateRating(Long ratingId, Double rating) {
        log.info("REQUEST (updateRating) FROM [feedback-service]");
        String userId = jwtTokenService.getCurrentUserId();
        return ratingClientFeign.updateRating(ratingId, userId, rating);
    }

    @Override
    public Object getRating(Long targetId, String targetType) {
        log.info("REQUEST (getRating) FROM [feedback-service]");
        return ratingClientFeign.getRating(targetId, targetType);
    }
}
