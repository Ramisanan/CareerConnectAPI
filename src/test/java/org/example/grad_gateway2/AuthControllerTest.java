package org.example.grad_gateway2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.grad_gateway2.DTO.RegisterDTO;
import org.example.grad_gateway2.Rest.AuthController;
import org.example.grad_gateway2.Services.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/auth/register forbids existing user")
    void register_existing() throws Exception {
        RegisterDTO dto = new RegisterDTO();
        dto.setEmail("foo");
        when(userService.userExists("foo")).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User already exists"));

        verify(userService, never()).addUser(any());
    }

    @Test
    @DisplayName("GET /api/auth/all returns users")
    void getAll() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());
        mockMvc.perform(get("/api/auth/all"))
                .andExpect(status().isOk());
    }
}