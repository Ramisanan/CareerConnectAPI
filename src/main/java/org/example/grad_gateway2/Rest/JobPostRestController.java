package org.example.grad_gateway2.Rest;

import jakarta.validation.Valid;
import org.example.grad_gateway2.DTO.JobPostDTO;
import org.example.grad_gateway2.DTO.JobPostResponseDTO;
import org.example.grad_gateway2.Services.JobPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/jobPost")
public class JobPostRestController {

    private final JobPostService jobPostService;

    @Autowired
    public JobPostRestController(JobPostService jobPostService) {
        this.jobPostService = jobPostService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addJobPost(@Valid @RequestBody JobPostDTO jobPostDTO){
        try{
            jobPostService.addJobPost(jobPostDTO);
            return ResponseEntity.ok("Job post added successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body("Error: "+e.getMessage());
        }

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateJobPost(@Valid @RequestBody JobPostDTO jobPostDTO, @PathVariable long id){
        try{
            jobPostService.updateJobPost(jobPostDTO, id);
            return ResponseEntity.ok("Job post updated successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteJobPost(@PathVariable long id){
        try{
            jobPostService.deleteJobPost(id);
            return ResponseEntity.ok("Job post deleted successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<JobPostResponseDTO>> getAllJobPosts(){
        return ResponseEntity.ok(jobPostService.getAllJobPosts());
    }

    @GetMapping("/jobPost/{id}")
    public ResponseEntity<?> getJobById(@PathVariable long id){
        JobPostResponseDTO jobPost;
        try {
            jobPost = jobPostService.getJobPostById(id);
            return ResponseEntity.ok(jobPost);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/jobPost/title")

    public ResponseEntity<List<JobPostResponseDTO>> getJobByTitle(@RequestParam String title){
        return ResponseEntity.ok(jobPostService.getJobPostByTitle(title));
    }

    @GetMapping("/jobPost/location")
    public ResponseEntity<List<JobPostResponseDTO>> getJobByLocation(@RequestParam String location){
        return ResponseEntity.ok(jobPostService.getJobPostByLocation(location));
    }

    @GetMapping("/jobPost/type")
    public ResponseEntity<List<JobPostResponseDTO>> getJobByType(@RequestParam String type){
        return ResponseEntity.ok(jobPostService.getJobPostByType(type));
    }

    @GetMapping("/jobPost/experience")
    public ResponseEntity<List<JobPostResponseDTO>> getJobByExperience(@RequestParam int experience){
        return ResponseEntity.ok(jobPostService.getJobPostByExperience(experience));
    }

    @GetMapping("/jobPost/salary")
    public ResponseEntity<List<JobPostResponseDTO>> getJobBySalary(@RequestParam int salary){
        return ResponseEntity.ok(jobPostService.getJobPostBySalary(salary));
    }

    @GetMapping("/jobPost/visaSponsorship")
    public ResponseEntity<List<JobPostResponseDTO>> getJobByVisaSponsorship(@RequestParam boolean visaSponsorship){
        return ResponseEntity.ok(jobPostService.getJobPostByVisaSponsorship(visaSponsorship));
    }

    @GetMapping("/jobPost/postedAt")
    public ResponseEntity<List<JobPostResponseDTO>> getJobByPostedAtAfter(@RequestParam String postedAt) throws ParseException {
        return ResponseEntity.ok(jobPostService.getJobPostByPostedAtAfter(postedAt));
    }


}
