package org.example.grad_gateway2.Rest;


import jakarta.validation.Valid;
import org.example.grad_gateway2.DTO.ApplicationDTO;
import org.example.grad_gateway2.Services.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/application")
public class ApplicationRestController {
    private final ApplicationService applicationService;

    @Autowired
    public ApplicationRestController
            (ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addApplication(@Valid @RequestBody ApplicationDTO applicationDTO){
       if (applicationService.existsByUserIdAndJobPostId(applicationDTO.getUserId(), applicationDTO.getJobPostId())) {
           return ResponseEntity.badRequest().body("Application already exists");
       }

         applicationService.addApplication(applicationDTO);
            return ResponseEntity.ok("Application added successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateApplication(@Valid @RequestBody
                                                    ApplicationDTO applicationDTO, @PathVariable long id){
        try{
            applicationService.updateApplication(applicationDTO, id);
            return ResponseEntity.ok("Application updated successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteApplication(@PathVariable long id){
        try{
            applicationService.deleteApplication(id);
            return ResponseEntity.ok("Application deleted successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/application/{id}")
    public ResponseEntity<?> getApplicationById(@PathVariable long id){
        ApplicationDTO applicationDTO;
        try {
            applicationDTO = applicationService.getApplicationById(id);
            return ResponseEntity.ok(applicationDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/applications")
    public ResponseEntity<?> getAllApplications(){
        try {
            return ResponseEntity.ok(applicationService.getAllApplications());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
