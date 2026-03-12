package org.example.grad_gateway2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.grad_gateway2.DTO.ReviewsDTO;
import org.example.grad_gateway2.DTO.ReviewsReponseDTO;
import org.example.grad_gateway2.Rest.ReviewRestController;
import org.example.grad_gateway2.Services.ReviewService;
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
@WebMvcTest(controllers = ReviewRestController.class)
class ReviewRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/review/all returns list")
    void getAll() throws Exception {
        when(reviewService.getAllReviews()).thenReturn(List.of(new ReviewsReponseDTO()));
        mockMvc.perform(get("/api/review/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("POST /api/review/add requires authentication")
    void addReview_authenticated() throws Exception {
        ReviewsDTO dto = new ReviewsDTO();
        dto.setCompanyId("1");
        when(reviewService.addReview(dto)).thenReturn(); // void

        mockMvc.perform(post("/api/review/add")
                        .with(user("u").roles("STUDENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }
}
