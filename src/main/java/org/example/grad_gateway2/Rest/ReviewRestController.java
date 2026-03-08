package org.example.grad_gateway2.Rest;

import jakarta.validation.Valid;
import org.example.grad_gateway2.DTO.ReviewsDTO;
import org.example.grad_gateway2.DTO.ReviewsReponseDTO;
import org.example.grad_gateway2.Services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/review")
public class ReviewRestController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewRestController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReviewsReponseDTO>> getAllReviews(){
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/review/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable long id){
        try {
            return ResponseEntity.ok(reviewService.getReviewById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<?> getReviewByCompanyId(@PathVariable long companyId){
        try {
            return ResponseEntity.ok(reviewService.getReviewByCompanyId(companyId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/company/{companyId}/order/{order}")
    public ResponseEntity<?> getReviewByCompanyIdOrderBy(@PathVariable long companyId, @PathVariable String order){
        try {
            return ResponseEntity.ok(reviewService.getReviewByCompanyIdOrderBy(companyId, order));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@Valid @RequestBody ReviewsDTO reviewsDTO){
        try{
            reviewService.addReview(reviewsDTO);
            return ResponseEntity.ok("Review added successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateReview(@Valid @RequestBody ReviewsDTO reviewsDTO, @PathVariable long id){
        try{
            reviewService.updateReview(reviewsDTO, id);
            return ResponseEntity.ok("Review updated successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReview(@PathVariable long id){
        try{
            reviewService.deleteReview(id);
            return ResponseEntity.ok("Review deleted successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
