package org.example.grad_gateway2.Services;

import org.example.grad_gateway2.DTO.ApplicationDTO;

import java.util.List;

public interface ApplicationService {

    void addApplication(ApplicationDTO applicationDTO);

    ApplicationDTO getApplicationById(long id);

    void updateApplication(ApplicationDTO applicationDTO, long id);

    void deleteApplication(long id);

    List<ApplicationDTO> getAllApplications();

    List<ApplicationDTO> getApplicationsByUserId(long userId);

    boolean existsByUserIdAndJobPostId(long userId, long jobPostId);

}
