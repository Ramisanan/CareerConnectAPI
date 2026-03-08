package org.example.grad_gateway2.DTO;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReviewsDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 1, message = "Rating must be greater than 0")
    @Max(value = 5, message = "Rating must be less than 6")
    private int rating;

    private long companyId;
}
