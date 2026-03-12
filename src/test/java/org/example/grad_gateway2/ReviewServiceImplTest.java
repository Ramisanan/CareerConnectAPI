package org.example.grad_gateway2;

import org.example.grad_gateway2.DAO.CompanyRepository;
import org.example.grad_gateway2.DAO.ReviewsRepository;
import org.example.grad_gateway2.DTO.ReviewsDTO;
import org.example.grad_gateway2.DTO.ReviewsReponseDTO;
import org.example.grad_gateway2.Entity.Company;
import org.example.grad_gateway2.Entity.Reviews;
import org.example.grad_gateway2.Entity.User;
import org.example.grad_gateway2.Services.ReviewServiceImpl;
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
class ReviewServiceImplTest {

    @Mock
    private ReviewsRepository reviewsRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private AuthenticationDetails authenticationDetails;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    private Company sampleCompany;
    private User sampleUser;
    private Reviews sampleReview;

    @BeforeEach
    void setUp() {
        sampleCompany = Company.builder().id(5L).name("Comp").build();
        sampleUser = User.builder().id(6L).firstName("Foo").lastName("Bar").build();
        sampleReview = Reviews.builder()
                .id(7L)
                .company(sampleCompany)
                .user(sampleUser)
                .rating(4)
                .title("T")
                .description("D")
                .postedAt(new Date())
                .build();
    }

    @Test
    void addReview_noCompany_throws() {
        ReviewsDTO dto = new ReviewsDTO();
        dto.setCompanyId("999");
        when(companyRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> reviewService.addReview(dto));
    }

    @Test
    void addReview_noUser_throws() {
        ReviewsDTO dto = new ReviewsDTO();
        dto.setCompanyId("5");
        when(companyRepository.findById(5L)).thenReturn(Optional.of(sampleCompany));
        when(authenticationDetails.getUser()).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> reviewService.addReview(dto));
    }

    @Test
    void addReview_success() {
        ReviewsDTO dto = new ReviewsDTO();
        dto.setCompanyId("5");
        dto.setRating(3);
        dto.setTitle("TT");
        dto.setDescription("DD");

        when(companyRepository.findById(5L)).thenReturn(Optional.of(sampleCompany));
        when(authenticationDetails.getUser()).thenReturn(Optional.of(sampleUser));

        reviewService.addReview(dto);
        ArgumentCaptor<Reviews> cap = ArgumentCaptor.forClass(Reviews.class);
        verify(reviewsRepository).save(cap.capture());
        assertEquals(sampleCompany, cap.getValue().getCompany());
    }

    @Test
    void deleteReview_missing_throws() {
        when(reviewsRepository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> reviewService.deleteReview(7L));
    }

    @Test
    void deleteReview_existing() {
        when(reviewsRepository.findById(7L)).thenReturn(Optional.of(sampleReview));
        reviewService.deleteReview(7L);
        verify(reviewsRepository).deleteById(7L);
    }

    @Test
    void getAllReviews() {
        when(reviewsRepository.findAll()).thenReturn(List.of(sampleReview));
        var list = reviewService.getAllReviews();
        assertEquals(1, list.size());
        assertEquals("Comp", list.get(0).getCompanyName());
    }

    @Test
    void getReviewById_missing() {
        when(reviewsRepository.findById(8L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> reviewService.getReviewById(8L));
    }

    @Test
    void getReviewById_found() {
        when(reviewsRepository.findById(7L)).thenReturn(Optional.of(sampleReview));
        var dto = reviewService.getReviewById(7L);
        assertEquals(7L, dto.getId());
    }

    @Test
    void getReviewByCompanyId_missingCompany() {
        when(companyRepository.findById(5L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> reviewService.getReviewByCompanyId(5L));
    }

    @Test
    void getReviewByCompanyId_returns() {
        when(companyRepository.findById(5L)).thenReturn(Optional.of(sampleCompany));
        when(reviewsRepository.findAllByCompanyId(5L)).thenReturn(List.of(sampleReview));
        var list = reviewService.getReviewByCompanyId(5L);
        assertEquals(1, list.size());
    }

    @Test
    void getReviewByCompanyIdOrderBy_asc() {
        when(companyRepository.findById(5L)).thenReturn(Optional.of(sampleCompany));
        when(reviewsRepository.findAllByCompanyIdOOrderByBest(5L)).thenReturn(List.of(sampleReview));
        var list = reviewService.getReviewByCompanyIdOrderBy(5L, "ASC");
        assertEquals(1, list.size());
    }

    @Test
    void getReviewByCompanyIdOrderBy_desc() {
        when(companyRepository.findById(5L)).thenReturn(Optional.of(sampleCompany));
        when(reviewsRepository.findAllByCompanyIdOrderByWorst(5L)).thenReturn(List.of(sampleReview));
        var list = reviewService.getReviewByCompanyIdOrderBy(5L, "DESC");
        assertEquals(1, list.size());
    }
}