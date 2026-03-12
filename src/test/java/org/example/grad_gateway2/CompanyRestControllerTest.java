package org.example.grad_gateway2;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.grad_gateway2.DTO.CompanyDTO;
import org.example.grad_gateway2.Entity.Company;
import org.example.grad_gateway2.Rest.CompanyRestController;
import org.example.grad_gateway2.Services.CompanyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CompanyRestController.class)
class CompanyRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /api/company/company/{id} returns 200 and company")
    void getCompanyById_success() throws Exception {
        Company c = Company.builder().id(1L).name("Acme").build();
        when(companyService.getCompanyById(1L)).thenReturn(c);

        mockMvc.perform(get("/api/company/company/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Acme"));
    }

    @Test
    @DisplayName("POST /api/company/add rejects existing company")
    void addCompany_exists() throws Exception {
        CompanyDTO dto = new CompanyDTO();
        dto.setName("Acme");
        dto.setLocation("NY");
        dto.setUrl("u");
        dto.setDescription("d");
        dto.setSize(10);

        when(companyService.existsByName("Acme")).thenReturn(true);

        mockMvc.perform(post("/api/company/add")
                        .with(user("emp").roles("EMPLOYER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Company already exists"));

        verify(companyService, never()).addCompany(any());
    }

    @Test
    @DisplayName("GET /api/company/all returns list")
    void getAllCompanies() throws Exception {
        Company c = Company.builder().id(1L).name("X").build();
        when(companyService.getAllCompanies()).thenReturn(List.of(c));

        mockMvc.perform(get("/api/company/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("POST /api/company/add forbids unauthenticated")
    void addCompany_unauthenticated() throws Exception {
        CompanyDTO dto = new CompanyDTO();
        dto.setName("NoAuth");
        dto.setLocation("NY");
        dto.setUrl("u");
        dto.setDescription("d");
        dto.setSize(1);

        mockMvc.perform(post("/api/company/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isForbidden());

        verify(companyService, never()).addCompany(any());
    }

    @Test
    @DisplayName("POST /api/company/add allows employer role")
    void addCompany_withRole() throws Exception {
        CompanyDTO dto = new CompanyDTO();
        dto.setName("NewCo");
        dto.setLocation("LA");
        dto.setUrl("http://newco.example");
        dto.setDescription("Cool");
        dto.setSize(5);

        when(companyService.existsByName("NewCo")).thenReturn(false);

        mockMvc.perform(post("/api/company/add")
                        .with(user("emp").roles("EMPLOYER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(companyService).addCompany(any());
    }
}
