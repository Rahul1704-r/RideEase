package com.Uber.Project.UberApp.Services;

import com.Uber.Project.UberApp.DTO.DriverDTO;
import com.Uber.Project.UberApp.DTO.SignupDTO;
import com.Uber.Project.UberApp.DTO.UserDTO;

public interface AuthenticationService {

    String[] Login(String email, String password);

    UserDTO SignUp(SignupDTO signupdto);

    DriverDTO onBoardNewDriver(Long userId,String vehicleId);

    String refreshToken(String refreshToken);
}
