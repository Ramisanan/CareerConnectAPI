package org.example.grad_gateway2.Services;


import org.example.grad_gateway2.DAO.CompanyRepository;
import org.example.grad_gateway2.DAO.JobPostRepository;
import org.example.grad_gateway2.DTO.CompanyDTO;
import org.example.grad_gateway2.DTO.JobPostDTO;
import org.example.grad_gateway2.DTO.JobPostResponseDTO;
import org.example.grad_gateway2.Entity.Company;
import org.example.grad_gateway2.Entity.JobPost;
import org.example.grad_gateway2.Entity.User;
import org.example.grad_gateway2.Util.AuthenticationDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JobPostServiceImpl implements JobPostService {
    private final JobPostRepository jobPostRepository;

    private final CompanyRepository companyRepository;

    private final AuthenticationDetails authenticationDetails;

    @Autowired
    public JobPostServiceImpl(JobPostRepository jobPostRepository, CompanyRepository companyRepository, AuthenticationDetails authenticationDetails) {
        this.jobPostRepository = jobPostRepository;
        this.companyRepository = companyRepository;
        this.authenticationDetails = authenticationDetails;
    }


    @Override
    public void addJobPost(JobPostDTO jobPostDTO) {

        Company company = companyRepository.findById(Long.valueOf(jobPostDTO.getCompanyId()))
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        Optional<User> user = authenticationDetails.getUser();

        if (user.isEmpty()) {
            throw new IllegalArgumentException("Unauthorized");
        }

        JobPost jobPost = JobPost.builder()
                .id(0L)
                .title(jobPostDTO.getTitle())
                .description(jobPostDTO.getDescription())
                .location(jobPostDTO.getLocation())
                .type(jobPostDTO.getType())
                .experience(jobPostDTO.getExperience())
                .salary(jobPostDTO.getSalary())
                .visaSponsorship(jobPostDTO.isVisaSponsorship())
                .postedAt(new Date())
                .company(company)
                .user(user.get())
                .build();
        jobPostRepository.save(jobPost);

    }

    @Override
    public void updateJobPost(JobPostDTO jobPostDTO, long id) {

        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job post not found"));

        if (jobPost != null) {
            jobPost.setTitle(jobPostDTO.getTitle());
            jobPost.setDescription(jobPostDTO.getDescription());
            jobPost.setLocation(jobPostDTO.getLocation());
            jobPost.setType(jobPostDTO.getType());
            jobPost.setExperience(jobPostDTO.getExperience());
            jobPost.setSalary(jobPostDTO.getSalary());
            jobPost.setVisaSponsorship(jobPostDTO.isVisaSponsorship());
            jobPostRepository.save(jobPost);
        }

    }

    @Override
    public void deleteJobPost(long id) {
        jobPostRepository.findById(id).orElseThrow(() -> new RuntimeException("Job post not found"));
        jobPostRepository.deleteById(id);

    }

    @Override
    public List<JobPostResponseDTO> getAllJobPosts() {
        List<JobPost> jobPosts = jobPostRepository.findAll();
        return jobPosts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public JobPostResponseDTO getJobPostById(long id) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job post not found"));
        return convertToDTO(jobPost);
    }

    @Override
    public List<JobPostResponseDTO> getJobPostByTitle(String title) {
        return jobPostRepository
                .findAllByTitleLikeIgnoreCase(title)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostResponseDTO> getJobPostByLocation(String location) {
        return jobPostRepository
                .findAllByLocationLikeIgnoreCase(location)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostResponseDTO> getJobPostByType(String type) {
        return jobPostRepository
                .findAllByTypeLikeIgnoreCase(type)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostResponseDTO> getJobPostByExperience(int experience) {
        return jobPostRepository
                .findAllByExperienceLessThanEqual(experience)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostResponseDTO> getJobPostBySalary(int salary) {
        return jobPostRepository
                .findAllBySalaryLessThanEqual(salary)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostResponseDTO> getJobPostByVisaSponsorship(boolean visaSponsorship) {
        return jobPostRepository
                .findAllByVisaSponsorship(visaSponsorship)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobPostResponseDTO> getJobPostByPostedAtAfter(String postedAt) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(postedAt);
        return jobPostRepository
                .findAllByPostedAtAfter(date)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private JobPostResponseDTO convertToDTO(JobPost jobPost) {
        return JobPostResponseDTO.builder()
                .id(jobPost.getId())
                .title(jobPost.getTitle())
                .description(jobPost.getDescription())
                .location(jobPost.getLocation())
                .type(jobPost.getType())
                .experience(jobPost.getExperience())
                .salary(jobPost.getSalary())
                .visaSponsorship(jobPost.isVisaSponsorship())
                .companyName(jobPost.getCompany().getName())
                .userFirstName(jobPost.getUser().getFirstName())
                .userLastName(jobPost.getUser().getLastName())
                .build();
    }

}
