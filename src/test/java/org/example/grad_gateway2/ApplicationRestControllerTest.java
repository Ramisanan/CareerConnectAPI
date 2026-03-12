package org.example.grad_gateway2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.grad_gateway2.DTO.ApplicationDTO;
import org.example.grad_gateway2.Rest.ApplicationRestController;
import org.example.grad_gateway2.Services.ApplicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ApplicationRestController.class)
class ApplicationRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApplicationService applicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/application/add rejects duplicate")
    void addApplication_duplicate() throws Exception {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setUserId(1L);
        dto.setJobPostId(2L);

        when(applicationService.existsByUserIdAndJobPostId(1L, 2L)).thenReturn(true);

        mockMvc.perform(post("/api/application/add")
                        .with(user("u").roles("STUDENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Application already exists"));

        verify(applicationService, never()).addApplication(any());
    }

    @Test
    @DisplayName("GET /api/application/application/{id} returns application")
    void getById_success() throws Exception {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(10L);
        when(applicationService.getApplicationById(10L)).thenReturn(dto);

        mockMvc.perform(get("/api/application/application/10").with(user("u").roles("STUDENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @Test
    @DisplayName("GET /api/application/applications returns list")
    void getAll() throws Exception {
        when(applicationService.getAllApplications()).thenReturn(List.of(new ApplicationDTO()));
        mockMvc.perform(get("/api/application/applications").with(user("u").roles("STUDENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}
