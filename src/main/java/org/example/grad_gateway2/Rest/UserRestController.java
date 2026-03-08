package org.example.grad_gateway2.Rest;

import jakarta.validation.Valid;
import org.example.grad_gateway2.DTO.UserDataDTO;
import org.example.grad_gateway2.Services.UserDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/user")
public class UserRestController {

    private final UserDataService userDataService;

    @Autowired
    public UserRestController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUserData(@Valid @RequestBody UserDataDTO userDataDTO) throws ParseException {
        try{
            userDataService.addUserData(userDataDTO);
            return ResponseEntity.ok("User data added successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUserData(@Valid @RequestBody UserDataDTO userDataDTO, @PathVariable long id){
        try{
            userDataService.updateUserData(userDataDTO, id);
            return ResponseEntity.ok("User data updated successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserDataByUserId(@PathVariable long userId){
        try {
            return ResponseEntity.ok(userDataService.getUserDataByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserData(@PathVariable long id){
        try{
            userDataService.deleteUserData(id);
            return ResponseEntity.ok("User data deleted successfully");
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
