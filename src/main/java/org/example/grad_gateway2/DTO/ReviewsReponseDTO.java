package org.example.grad_gateway2.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewsReponseDTO {

    private long id;

    private String title;

    private String description;

    private int rating;

    private String companyName;

    private String userFirstName;

    private String userLastName;

    private String postedAt;
}
