package org.example.grad_gateway2.Services;

import org.example.grad_gateway2.DTO.JobPostDTO;
import org.example.grad_gateway2.DTO.JobPostResponseDTO;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface JobPostService {

    void addJobPost(JobPostDTO jobPostDTO);

    void updateJobPost(JobPostDTO jobPostDTO, long id);

    void deleteJobPost(long id);

    List<JobPostResponseDTO> getAllJobPosts();

    JobPostResponseDTO getJobPostById(long id);

    List<JobPostResponseDTO> getJobPostByTitle(String title);

    List<JobPostResponseDTO> getJobPostByLocation(String location);

    List<JobPostResponseDTO> getJobPostByType(String type);

    List<JobPostResponseDTO> getJobPostByExperience(int experience);

    List<JobPostResponseDTO> getJobPostBySalary(int salary);

    List<JobPostResponseDTO> getJobPostByVisaSponsorship(boolean visaSponsorship);

    List<JobPostResponseDTO> getJobPostByPostedAtAfter(String postedAt) throws ParseException;
}
