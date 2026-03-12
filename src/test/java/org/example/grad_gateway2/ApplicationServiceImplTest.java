package org.example.grad_gateway2;

import org.example.grad_gateway2.DAO.ApplicationRepository;
import org.example.grad_gateway2.DAO.JobPostRepository;
import org.example.grad_gateway2.DTO.ApplicationDTO;
import org.example.grad_gateway2.Entity.Applications;
import org.example.grad_gateway2.Entity.JobPost;
import org.example.grad_gateway2.Entity.User;
import org.example.grad_gateway2.Services.ApplicationServiceImpl;
import org.example.grad_gateway2.Util.AuthenticationDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private JobPostRepository jobPostRepository;

    @Mock
    private AuthenticationDetails authenticationDetails;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    private User sampleUser;
    private JobPost sampleJobPost;
    private Applications sampleApp;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder().id(100L).email("u@ex").build();
        sampleJobPost = JobPost.builder().id(200L).title("X").build();
        sampleApp = Applications.builder()
                .id(10L)
                .user(sampleUser)
                .jobPost(sampleJobPost)
                .status("PENDING")
                .applicationDate(new Date())
                .build();
    }

    @Test
    void addApplication_noAuth_shouldThrow() {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setJobPostId(200L);

        when(authenticationDetails.getUser()).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> applicationService.addApplication(dto));
    }

    @Test
    void addApplication_jobPostMissing_shouldThrow() {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setJobPostId(999L);

        when(authenticationDetails.getUser()).thenReturn(Optional.of(sampleUser));
        when(jobPostRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> applicationService.addApplication(dto));
    }

    @Test
    void addApplication_success() {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setJobPostId(200L);
        dto.setStatus("PENDING");
        dto.setApplicationDate(new Date());

        when(authenticationDetails.getUser()).thenReturn(Optional.of(sampleUser));
        when(jobPostRepository.findById(200L)).thenReturn(Optional.of(sampleJobPost));

        applicationService.addApplication(dto);

        ArgumentCaptor<Applications> captor = ArgumentCaptor.forClass(Applications.class);
        verify(applicationRepository).save(captor.capture());
        assertEquals(sampleUser, captor.getValue().getUser());
        assertEquals(sampleJobPost, captor.getValue().getJobPost());
    }

    @Test
    void getApplicationById_found() {
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(sampleApp));
        var dto = applicationService.getApplicationById(10L);
        assertEquals(10L, dto.getId());
        assertEquals(100L, dto.getUserId());
    }

    @Test
    void getApplicationById_notFound() {
        when(applicationRepository.findById(20L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> applicationService.getApplicationById(20L));
    }

    @Test
    void updateApplication_existing_updatesStatus() {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setStatus("ACCEPTED");
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(sampleApp));
        applicationService.updateApplication(dto, 10L);

        assertEquals("ACCEPTED", sampleApp.getStatus());
        verify(applicationRepository).save(sampleApp);
    }

    @Test
    void deleteApplication_existing() {
        when(applicationRepository.findById(10L)).thenReturn(Optional.of(sampleApp));
        applicationService.deleteApplication(10L);
        verify(applicationRepository).deleteById(10L);
    }

    @Test
    void deleteApplication_missing_throws() {
        when(applicationRepository.findById(11L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> applicationService.deleteApplication(11L));
    }

    @Test
    void getAllApplications_returnsConvertedList() {
        when(applicationRepository.findAll()).thenReturn(List.of(sampleApp));
        var list = applicationService.getAllApplications();
        assertEquals(1, list.size());
    }

    @Test
    void getApplicationsByUserId() {
        when(applicationRepository.findAllByUserId(100L)).thenReturn(List.of(sampleApp));
        var list = applicationService.getApplicationsByUserId(100L);
        assertEquals(1, list.size());
    }

    @Test
    void existsByUserIdAndJobPostId_delegates() {
        when(applicationRepository.existsByUserIdAndJobPostId(100L, 200L)).thenReturn(true);
        assertTrue(applicationService.existsByUserIdAndJobPostId(100L, 200L));
    }
}
