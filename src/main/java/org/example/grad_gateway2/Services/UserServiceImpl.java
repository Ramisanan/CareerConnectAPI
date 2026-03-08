package org.example.grad_gateway2.Services;

import org.example.grad_gateway2.DAO.CompanyRepository;
import org.example.grad_gateway2.DAO.UserRepository;
import org.example.grad_gateway2.DTO.RegisterDTO;
import org.example.grad_gateway2.DTO.UserResponseDTO;
import org.example.grad_gateway2.Entity.User;
import org.example.grad_gateway2.Util.AuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final CompanyRepository companyRepository;

    private PasswordEncoder passwordEncoder;

    private final AuthenticationDetails authenticationDetails;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, CompanyRepository companyRepository,
                           PasswordEncoder passwordEncoder, AuthenticationDetails authenticationDetails) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationDetails = authenticationDetails;
    }



    @Override
    public void addUser(RegisterDTO registerDTO) {

        User user = User.builder()
                .id(0L)
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .firstName(registerDTO.getFirstName())
                .lastName(registerDTO.getLastName())
                .role(registerDTO.getRole())
                .build();
        if (user.getRole().equals("EMPLOYER")) {
            user.setCompany(companyRepository.findById(registerDTO.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Company not found")));
        }
        userRepository.save(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToUserResponseDTO)
                .toList();
    }

    @Override
    public UserResponseDTO getUserById(long id) {
        Optional<User> user = authenticationDetails.getUser();
        if(user.isPresent() && !user.get().getRole().equals("ADMIN") && user.get().getId() != id){
            throw new IllegalArgumentException("Unauthorized");
        }
        return convertToUserResponseDTO((User) userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
    }

    @Override
    public void deleteUser(long id) {
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(id);
    }



    @Override
    public boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }

    private UserResponseDTO convertToUserResponseDTO(User user){
        return UserResponseDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .comapanyName(user.getCompany() != null ? user.getCompany().getName() : null)
                .build();
    }
}
