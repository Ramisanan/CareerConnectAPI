package org.example.grad_gateway2.Services;

import org.example.grad_gateway2.DAO.CompanyRepository;
import org.example.grad_gateway2.DAO.JobPostRepository;
import org.example.grad_gateway2.DTO.RegisterDTO;
import org.example.grad_gateway2.DTO.UserDataDTO;
import org.example.grad_gateway2.DTO.UserResponseDTO;
import org.example.grad_gateway2.Util.AuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;

import java.util.List;

public interface UserService {


    void addUser(RegisterDTO registerDTO);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(long id);

    void deleteUser(long id);

    boolean userExists(String email);
}
