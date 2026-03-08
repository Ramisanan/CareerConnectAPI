package org.example.grad_gateway2.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class CompanyDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "URL is required")
    @URL(message = "URL is invalid")
    private String url;

    @NotBlank(message = "Description is required")
    private String description;

    @Min(value = 1, message = "Size must be greater than 0")
    private int size;
}
