package org.example.grad_gateway2.Services;

import org.example.grad_gateway2.DTO.UserDataDTO;
import org.example.grad_gateway2.Entity.UserData;

import java.text.ParseException;

public interface UserDataService {

    void addUserData(UserDataDTO userDataDTO) throws ParseException;

    void updateUserData(UserDataDTO userDataDTO, long id)throws ParseException;

    UserData getUserDataByUserId(long userId);

    void deleteUserData(long id);
}
