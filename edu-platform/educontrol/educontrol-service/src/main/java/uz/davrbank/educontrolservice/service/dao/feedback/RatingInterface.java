package uz.davrbank.educontrolservice.service.dao.feedback;

import uz.davrbank.educontrolservice.aggregate.dto.rating.RatingRequestDTO;

public interface RatingInterface {

    Object addRating(RatingRequestDTO requestDTO);

    Object removeRating(Long ratingId);

    Object updateRating(Long ratingId, Double rating);

    Object getRating(Long targetId, String targetType);
}
