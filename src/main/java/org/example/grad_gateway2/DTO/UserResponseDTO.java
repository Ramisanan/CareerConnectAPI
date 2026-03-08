package org.example.grad_gateway2.DTO;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {

    private long id;

    private String email;

    private String role;

    private String firstName;

    private String lastName;

    private String comapanyName;


}
