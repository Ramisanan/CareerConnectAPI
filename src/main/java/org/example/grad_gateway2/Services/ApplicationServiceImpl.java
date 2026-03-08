package org.example.grad_gateway2.Services;

import org.example.grad_gateway2.DAO.ApplicationRepository;
import org.example.grad_gateway2.DAO.JobPostRepository;
import org.example.grad_gateway2.DTO.ApplicationDTO;
import org.example.grad_gateway2.Entity.Applications;
import org.example.grad_gateway2.Entity.JobPost;
import org.example.grad_gateway2.Entity.User;
import org.example.grad_gateway2.Util.AuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService{

    private final ApplicationRepository applicationRepository;

    private final AuthenticationDetails authenticationDetails;

    private final JobPostRepository jobPostRepository;

    @Autowired
    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  JobPostRepository jobPostRepository,
                                  AuthenticationDetails authenticationDetails) {
        this.applicationRepository = applicationRepository;
        this.jobPostRepository = jobPostRepository;
        this.authenticationDetails = authenticationDetails;
    }

    @Override
    public void addApplication(ApplicationDTO applicationDTO) {
        Optional<User> user = authenticationDetails.getUser();
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Unauthorized");
        }
        JobPost jobPost = jobPostRepository.findById(applicationDTO.getJobPostId())
                .orElseThrow(() -> new RuntimeException("Job Post not found"+
                        applicationDTO.getJobPostId()));

        Applications applications = Applications.builder()
                .user(user.get())
                .jobPost(jobPost)
                .applicationDate(applicationDTO.getApplicationDate())
                .status(applicationDTO.getStatus())
                .build();
        applicationRepository.save(applications);

    }

    @Override
    public ApplicationDTO getApplicationById(long id) {
        Applications applications =  applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        return convertToDTO(applications);

    }

    @Override
    public void updateApplication(ApplicationDTO applicationDTO, long id) {

        Applications applications = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        if ( applications != null){
            applications.setStatus(applicationDTO.getStatus());
            // coz we only allow employer role to update the status in the application
            applicationRepository.save(applications);
        }

    }

    @Override
    public void deleteApplication(long id) {
        applicationRepository.findById(id).
                orElseThrow(() -> new RuntimeException
                        ("Application not found"));
        applicationRepository.deleteById(id);

    }

    @Override
    public List<ApplicationDTO> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<ApplicationDTO> getApplicationsByUserId(long userId) {
        return applicationRepository.findAllByUserId(userId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public boolean existsByUserIdAndJobPostId(long userId,
                                              long jobPostId) {
        return applicationRepository.existsByUserIdAndJobPostId(userId, jobPostId);
    }

    private  ApplicationDTO convertToDTO(Applications applications) {
        return ApplicationDTO.builder()
                .id(applications.getId())
                .userId(applications.getUser().getId())
                .jobPostId(applications.getJobPost().getId())
                .applicationDate(applications.getApplicationDate())
                .status(applications.getStatus())
                .build();
    }
}
