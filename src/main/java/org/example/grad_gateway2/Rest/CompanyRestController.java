package org.example.grad_gateway2.Rest;


import jakarta.validation.Valid;
import org.example.grad_gateway2.DTO.CompanyDTO;
import org.example.grad_gateway2.Entity.Company;
import org.example.grad_gateway2.Services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/company")
public class CompanyRestController {

    private final CompanyService companyService;

    @Autowired
    public CompanyRestController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable long id){
        Company company;
        try {
            company = companyService.getCompanyById(id);
            return ResponseEntity.ok(company);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCompany(@Valid @RequestBody CompanyDTO companyDTO){
        if(companyService.existsByName(companyDTO.getName())){
            return ResponseEntity.badRequest().body("Company already exists");
        }
        companyService.addCompany(companyDTO);
        return ResponseEntity.ok("Company added successfully");
    }

    @PutMapping("/update/{id}")

    public ResponseEntity<String> updateCompany(@Valid @RequestBody CompanyDTO companyDTO,
                                                @PathVariable long id){
        try{
            companyService.updateCompany(companyDTO, id);
            return ResponseEntity.ok("Company updated successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/delete/{id}")

    public ResponseEntity<String> deleteCompany(@PathVariable long id){
        try{
            companyService.deleteCompany(id);
            return ResponseEntity.ok("Company deleted successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/company/name")
    public ResponseEntity<?> getCompany(@RequestParam String name){
        return ResponseEntity.ok(companyService.getCompany(name));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCompanies(){
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/company/location")
    public ResponseEntity<?> getCompanyByLocation(@RequestParam String location) {
        return ResponseEntity.ok(companyService.getCompanyByLocation(location));

    }

    @GetMapping("/company/size")
    public ResponseEntity<?> getCompanyBySize(@RequestParam int size) {
        return ResponseEntity.ok(companyService.getCompanyBySize(size));
    }

    @PostMapping("/image/")
    public ResponseEntity<String>  uploadImage(@RequestParam("image") MultipartFile file,
                                               @RequestParam("id") long id) {
        try {
            String s = companyService.uploadImage(file, id);
            return ResponseEntity.ok(s);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/image/{id}")
    public ResponseEntity<?> getImage(@PathVariable long id){
        try {
            byte[] image = companyService.getImage(id);
            return ResponseEntity.status(200)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(image);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/exists")
    public ResponseEntity<Boolean> existsByName(@RequestParam String name){
        return ResponseEntity.ok(companyService.existsByName(name));
    }
}
