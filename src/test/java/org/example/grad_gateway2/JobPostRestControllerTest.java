package org.example.grad_gateway2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.grad_gateway2.DTO.JobPostDTO;
import org.example.grad_gateway2.DTO.JobPostResponseDTO;
import org.example.grad_gateway2.Rest.JobPostRestController;
import org.example.grad_gateway2.Services.JobPostService;
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
@WebMvcTest(controllers = JobPostRestController.class)
class JobPostRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JobPostService jobPostService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/jobPost/all returns list")
    void getAll() throws Exception {
        when(jobPostService.getAllJobPosts()).thenReturn(List.of(new JobPostResponseDTO()));
        mockMvc.perform(get("/api/jobPost/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("POST /api/jobPost/add requires auth")
    void add_requiresAuth() throws Exception {
        JobPostDTO dto = new JobPostDTO();
        when(jobPostService.addJobPost(dto)).thenReturn();

        mockMvc.perform(post("/api/jobPost/add")
                        .with(user("u").roles("EMPLOYER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}
