package org.example.grad_gateway2.DTO;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ApplicationDTO {

    private Long id;
    private Long userId;
    private Long jobPostId;
    private Date applicationDate;
    private String status;

}
