package org.example.grad_gateway2.Services;


import org.example.grad_gateway2.DAO.CompanyRepository;
import org.example.grad_gateway2.DAO.ReviewsRepository;
import org.example.grad_gateway2.DTO.ReviewsDTO;
import org.example.grad_gateway2.DTO.ReviewsReponseDTO;
import org.example.grad_gateway2.Entity.Company;
import org.example.grad_gateway2.Entity.Reviews;
import org.example.grad_gateway2.Entity.User;
import org.example.grad_gateway2.Util.AuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;



@Service
public class ReviewServiceImpl implements ReviewService{

    private final ReviewsRepository reviewsRepository;

    private final CompanyRepository companyRepository;

    private final AuthenticationDetails authenticationDetails;

    @Autowired
    public ReviewServiceImpl(ReviewsRepository reviewsRepository, CompanyRepository companyRepository, AuthenticationDetails authenticationDetails) {
        this.reviewsRepository = reviewsRepository;
        this.companyRepository = companyRepository;
        this.authenticationDetails = authenticationDetails;
    }
    @Override
    public void addReview(ReviewsDTO reviewsDTO) {

        Company company = companyRepository.findById(Long.valueOf(reviewsDTO.getCompanyId()))
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        Optional<User> user = authenticationDetails.getUser();

        if(user.isEmpty()){
            throw new IllegalArgumentException("Unauthorized");
        }

        Reviews reviews = Reviews.builder()
                .id(0)
                .title(reviewsDTO.getTitle())
                .description(reviewsDTO.getDescription())
                .rating(reviewsDTO.getRating())
                .company(company)
                .user(user.get())
                .postedAt(new Date())
                .build();
        reviewsRepository.save(reviews);

    }

    @Override
    public void updateReview(ReviewsDTO reviewDTO, long id) {
        Reviews reviews = reviewsRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

    }

    @Override
    public void deleteReview(long id) {
        reviewsRepository.findById(Long.valueOf(id)).orElseThrow(() -> new IllegalArgumentException("Review not found"));
        reviewsRepository.deleteById(id);

    }

    @Override
    public List<ReviewsReponseDTO> getAllReviews() {
        return reviewsRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public ReviewsReponseDTO getReviewById(long id) {
        Reviews reviews = reviewsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));
        return convertToDTO(reviews);
    }

    @Override
    public List<ReviewsReponseDTO> getReviewByCompanyId(long companyId) {
        if(companyRepository.findById(companyId).isEmpty()){
            throw new IllegalArgumentException("Company not found");
        }
        return reviewsRepository.findAllByCompanyId(companyId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<ReviewsReponseDTO> getReviewByCompanyIdOrderBy(long companyId, String order) {
        if(companyRepository.findById(companyId).isEmpty()){
            throw new IllegalArgumentException("Company not found");
        }
        if(order.equals("ASC")){
            return reviewsRepository.findAllByCompanyIdOOrderByBest(companyId)
                    .stream()
                    .map(this::convertToDTO)
                    .toList();
        }
        return reviewsRepository.findAllByCompanyIdOrderByWorst(companyId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    private ReviewsReponseDTO convertToDTO(Reviews reviews){
        return ReviewsReponseDTO.builder()
                .id(reviews.getId())
                .title(reviews.getTitle())
                .description(reviews.getDescription())
                .rating(reviews.getRating())
                .companyName(reviews.getCompany().getName())
                .userFirstName(reviews.getUser().getFirstName())
                .userLastName(reviews.getUser().getLastName())
                .postedAt(reviews.getPostedAt().toString())
                .build();
    }
}
