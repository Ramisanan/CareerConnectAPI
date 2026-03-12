package org.example.grad_gateway2;

import org.example.grad_gateway2.DAO.CompanyRepository;
import org.example.grad_gateway2.DAO.UserRepository;
import org.example.grad_gateway2.DTO.RegisterDTO;
import org.example.grad_gateway2.Entity.Company;
import org.example.grad_gateway2.Entity.User;
import org.example.grad_gateway2.Services.UserServiceImpl;
import org.example.grad_gateway2.Util.AuthenticationDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationDetails authenticationDetails;

    @InjectMocks
    private UserServiceImpl userService;

    private User exampleUser;

    @BeforeEach
    void setUp() {
        exampleUser = User.builder()
                .id(1L)
                .email("alice@example.com")
                .password("encoded")
                .firstName("Alice")
                .lastName("Smith")
                .role("USER")
                .build();
    }

    @Test
    void addUser_shouldSaveUserWithEncodedPassword() {
        RegisterDTO dto = new RegisterDTO();
        dto.setEmail("bob@example.com");
        dto.setPassword("plain");
        dto.setFirstName("Bob");
        dto.setLastName("Jones");
        dto.setRole("USER");

        when(passwordEncoder.encode("plain")).thenReturn("encoded");

        userService.addUser(dto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("bob@example.com", saved.getEmail());
        assertEquals("encoded", saved.getPassword());
        assertEquals("USER", saved.getRole());
    }

    @Test
    void getAllUsers_shouldReturnConvertedList() {
        when(userRepository.findAll()).thenReturn(List.of(exampleUser));
        var results = userService.getAllUsers();
        assertEquals(1, results.size());
        assertEquals("alice@example.com", results.get(0).getEmail());
    }

    @Test
    void getUserById_nonAdminAndDifferentId_shouldThrow() {
        when(authenticationDetails.getUser()).thenReturn(Optional.of(exampleUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(exampleUser));

        assertThrows(IllegalArgumentException.class, () -> userService.getUserById(2L));
    }

    @Test
    void userExists_returnsRepositoryValue() {
        when(userRepository.existsByEmail("foo")).thenReturn(true);
        assertTrue(userService.userExists("foo"));
    }

    @Test
    void deleteUser_shouldDeleteExisting() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(exampleUser));
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_nonExisting_shouldThrow() {
        when(userRepository.findById(42L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.deleteUser(42L));
    }
}
