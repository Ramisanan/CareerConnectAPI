package org.example.grad_gateway2.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")

    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
