package org.example.grad_gateway2;

import org.example.grad_gateway2.DAO.CompanyRepository;
import org.example.grad_gateway2.DAO.JobPostRepository;
import org.example.grad_gateway2.DTO.JobPostDTO;
import org.example.grad_gateway2.Entity.Company;
import org.example.grad_gateway2.Entity.JobPost;
import org.example.grad_gateway2.Entity.User;
import org.example.grad_gateway2.Services.JobPostServiceImpl;
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
class JobPostServiceImplTest {

    @Mock
    private JobPostRepository jobPostRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private AuthenticationDetails authenticationDetails;

    @InjectMocks
    private JobPostServiceImpl jobPostService;

    private Company sampleCompany;
    private User sampleUser;
    private JobPost sampleJob;

    @BeforeEach
    void setUp() {
        sampleCompany = Company.builder().id(1L).name("C").build();
        sampleUser = User.builder().id(2L).firstName("A").lastName("B").build();
        sampleJob = JobPost.builder().id(3L).title("T").company(sampleCompany).user(sampleUser).build();
    }

    @Test
    void addJobPost_noCompany_throws() {
        JobPostDTO dto = new JobPostDTO();
        dto.setCompanyId("999");
        when(companyRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> jobPostService.addJobPost(dto));
    }

    @Test
    void addJobPost_noUser_throws() {
        JobPostDTO dto = new JobPostDTO();
        dto.setCompanyId("1");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(sampleCompany));
        when(authenticationDetails.getUser()).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> jobPostService.addJobPost(dto));
    }

    @Test
    void addJobPost_success() {
        JobPostDTO dto = new JobPostDTO();
        dto.setCompanyId("1");
        dto.setTitle("Hello");
        dto.setLocation("L");
        when(companyRepository.findById(1L)).thenReturn(Optional.of(sampleCompany));
        when(authenticationDetails.getUser()).thenReturn(Optional.of(sampleUser));
        jobPostService.addJobPost(dto);
        ArgumentCaptor<JobPost> cap = ArgumentCaptor.forClass(JobPost.class);
        verify(jobPostRepository).save(cap.capture());
        assertEquals("Hello", cap.getValue().getTitle());
    }

    @Test
    void deleteJobPost_missing_throws() {
        when(jobPostRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> jobPostService.deleteJobPost(5L));
    }

    @Test
    void deleteJobPost_existing() {
        when(jobPostRepository.findById(3L)).thenReturn(Optional.of(sampleJob));
        jobPostService.deleteJobPost(3L);
        verify(jobPostRepository).deleteById(3L);
    }

    @Test
    void getAllJobPosts_returns() {
        when(jobPostRepository.findAll()).thenReturn(List.of(sampleJob));
        var list = jobPostService.getAllJobPosts();
        assertEquals(1, list.size());
    }

    @Test
    void getJobPostById_notFound() {
        when(jobPostRepository.findById(4L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> jobPostService.getJobPostById(4L));
    }

    @Test
    void getJobPostById_found() {
        when(jobPostRepository.findById(3L)).thenReturn(Optional.of(sampleJob));
        var dto = jobPostService.getJobPostById(3L);
        assertEquals(3L, dto.getId());
    }

    @Test
    void queryMethods_delegate() {
        when(jobPostRepository.findAllByTitleLikeIgnoreCase("t")).thenReturn(List.of(sampleJob));
        assertEquals(1, jobPostService.getJobPostByTitle("t").size());
        when(jobPostRepository.findAllByLocationLikeIgnoreCase("l")).thenReturn(List.of(sampleJob));
        assertEquals(1, jobPostService.getJobPostByLocation("l").size());
        when(jobPostRepository.findAllByTypeLikeIgnoreCase("x")).thenReturn(List.of(sampleJob));
        assertEquals(1, jobPostService.getJobPostByType("x").size());
        when(jobPostRepository.findAllByExperienceLessThanEqual(5)).thenReturn(List.of(sampleJob));
        assertEquals(1, jobPostService.getJobPostByExperience(5).size());
        when(jobPostRepository.findAllBySalaryLessThanEqual(10)).thenReturn(List.of(sampleJob));
        assertEquals(1, jobPostService.getJobPostBySalary(10).size());
        when(jobPostRepository.findAllByVisaSponsorship(false)).thenReturn(List.of(sampleJob));
        assertEquals(1, jobPostService.getJobPostByVisaSponsorship(false).size());
    }

}