package org.example.grad_gateway2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.grad_gateway2.DTO.UserDataDTO;
import org.example.grad_gateway2.Entity.UserData;
import org.example.grad_gateway2.Rest.UserRestController;
import org.example.grad_gateway2.Services.UserDataService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserRestController.class)
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDataService userDataService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/user/add returns 200 on success")
    void addUserData_success() throws Exception {
        UserDataDTO dto = new UserDataDTO();
        dto.setUserId(1L);
        dto.setMobileNumber("123");
        dto.setAddress("addr");

        doNothing().when(userDataService).addUserData(dto);

        mockMvc.perform(post("/api/user/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string("User data added successfully"));
    }

    @Test
    @DisplayName("GET /api/user/user/{userId} returns data")
    void getUserDataByUserId_success() throws Exception {
        UserData ud = UserData.builder().id(2L).userId(1L).mobileNumber("123").build();
        when(userDataService.getUserDataByUserId(1L)).thenReturn(ud);

        mockMvc.perform(get("/api/user/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
    }
}
