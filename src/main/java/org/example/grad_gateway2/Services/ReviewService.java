package org.example.grad_gateway2.Services;

import org.example.grad_gateway2.DTO.ReviewsDTO;
import org.example.grad_gateway2.DTO.ReviewsReponseDTO;

import java.util.List;

public interface ReviewService {

    void addReview(ReviewsDTO reviewDTO);

    void updateReview(ReviewsDTO reviewDTO, long id);

    void deleteReview(long id);

    List<ReviewsReponseDTO> getAllReviews();

    ReviewsReponseDTO getReviewById(long id);

    List<ReviewsReponseDTO> getReviewByCompanyId(long companyId);

    List<ReviewsReponseDTO> getReviewByCompanyIdOrderBy(long companyId, String order);
}
