package org.example.grad_gateway2;

import org.example.grad_gateway2.DAO.CompanyRepository;
import org.example.grad_gateway2.DTO.CompanyDTO;
import org.example.grad_gateway2.Entity.Company;
import org.example.grad_gateway2.Services.CompanyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company sampleCompany;

    @BeforeEach
    void setUp() {
        sampleCompany = Company.builder()
                .id(1L)
                .name("Acme")
                .location("New York")
                .url("http://acme.example")
                .description("A company")
                .size(100)
                .logo(null)
                .build();
    }

    @Test
    void getCompanyById_existing_shouldReturn() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(sampleCompany));
        Company result = companyService.getCompanyById(1L);
        assertSame(sampleCompany, result);
    }

    @Test
    void getCompanyById_missing_shouldThrow() {
        when(companyRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> companyService.getCompanyById(2L));
    }

    @Test
    void addCompany_shouldSaveNewEntity() {
        CompanyDTO dto = new CompanyDTO();
        dto.setName("NewCo");
        dto.setLocation("LA");
        dto.setUrl("http://newco.example");
        dto.setDescription("Cool");
        dto.setSize(5);

        companyService.addCompany(dto);
        ArgumentCaptor<Company> captor = ArgumentCaptor.forClass(Company.class);
        verify(companyRepository).save(captor.capture());
        Company saved = captor.getValue();
        assertEquals("NewCo", saved.getName());
        assertEquals("LA", saved.getLocation());
        assertEquals("http://newco.example", saved.getUrl());
        assertEquals("Cool", saved.getDescription());
        assertEquals(5, saved.getSize());
    }

    @Test
    void deleteCompany_existing_shouldCallRepository() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(sampleCompany));
        companyService.deleteCompany(1L);
        verify(companyRepository).deleteById(1L);
    }

    @Test
    void deleteCompany_missing_shouldThrow() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> companyService.deleteCompany(99L));
    }

    @Test
    void existsByName_delegatesToRepository() {
        when(companyRepository.existsByName("foo")).thenReturn(true);
        assertTrue(companyService.existsByName("foo"));
    }
}