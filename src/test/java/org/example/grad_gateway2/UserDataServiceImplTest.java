package org.example.grad_gateway2;

import org.example.grad_gateway2.DAO.UserDataRepository;
import org.example.grad_gateway2.DTO.UserDataDTO;
import org.example.grad_gateway2.Entity.User;
import org.example.grad_gateway2.Entity.UserData;
import org.example.grad_gateway2.Services.UserDataServiceImpl;
import org.example.grad_gateway2.Util.AuthenticationDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDataServiceImplTest {

    @Mock
    private UserDataRepository userDataRepository;

    @Mock
    private AuthenticationDetails authenticationDetails;

    @InjectMocks
    private UserDataServiceImpl userDataService;

    private User sampleUser;
    private UserData sampleData;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder().id(1L).role("USER").build();
        sampleData = UserData.builder().id(2L).user(sampleUser).build();
    }

    @Test
    void addUserData_noUser_throws() {
        UserDataDTO dto = new UserDataDTO();
        when(authenticationDetails.getUser()).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userDataService.addUserData(dto));
    }

    @Test
    void addUserData_success() throws ParseException {
        UserDataDTO dto = new UserDataDTO();
        dto.setMobile("123");
        dto.setAddress("addr");
        dto.setDob("2000-01-01");
        dto.setExperience("5");
        when(authenticationDetails.getUser()).thenReturn(Optional.of(sampleUser));
        userDataService.addUserData(dto);
        verify(userDataRepository).save(any(UserData.class));
    }

    @Test
    void updateUserData_unauthorized_throws() throws ParseException {
        UserDataDTO dto = new UserDataDTO();
        dto.setMobile("m");
        when(authenticationDetails.getUser()).thenReturn(Optional.of(sampleUser));
        // sampleUser id 1, update id 2 -> unauthorized
        assertThrows(IllegalArgumentException.class, () -> userDataService.updateUserData(dto, 2L));
    }

    @Test
    void updateUserData_success() throws ParseException {
        UserDataDTO dto = new UserDataDTO();
        dto.setMobile("m2");
        dto.setAddress("a");
        dto.setDob("2001-02-02");
        dto.setExperience("e");
        sampleUser.setRole("ADMIN");
        when(authenticationDetails.getUser()).thenReturn(Optional.of(sampleUser));
        when(userDataRepository.findById(2L)).thenReturn(Optional.of(sampleData));
        userDataService.updateUserData(dto, 2L);
        verify(userDataRepository).save(sampleData);
    }

    @Test
    void getUserDataByUserId_unauthorized() {
        when(authenticationDetails.getUser()).thenReturn(Optional.of(sampleUser));
        sampleUser.setRole("USER");
        sampleUser.setId(5L);
        assertThrows(IllegalArgumentException.class, () -> userDataService.getUserDataByUserId(1L));
    }

    @Test
    void getUserDataByUserId_success() {
        when(authenticationDetails.getUser()).thenReturn(Optional.of(sampleUser));
        when(userDataRepository.findByUserId(1L)).thenReturn(sampleData);
        sampleUser.setId(1L);
        UserData result = userDataService.getUserDataByUserId(1L);
        assertSame(sampleData, result);
    }

    @Test
    void deleteUserData_missing_throws() {
        when(userDataRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> userDataService.deleteUserData(2L));
    }

    @Test
    void deleteUserData_existing() {
        when(userDataRepository.findById(2L)).thenReturn(Optional.of(sampleData));
        userDataService.deleteUserData(2L);
        verify(userDataRepository).deleteById(2L);
    }
}