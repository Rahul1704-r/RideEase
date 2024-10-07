package com.Uber.Project.UberApp.Controllers;

import com.Uber.Project.UberApp.DTO.*;
import com.Uber.Project.UberApp.Services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor

public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO,
                                           HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        String tokens[]=authenticationService.Login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

        Cookie cookie= new Cookie("token",tokens[1]);
        cookie.setSecure(true);

        httpServletResponse.addCookie(cookie);


        return ResponseEntity.ok(new LoginResponseDTO(tokens[0]));
    }

    @PostMapping("/signup")
    ResponseEntity<UserDTO> signup(@RequestBody SignupDTO signupDTO){
        return ResponseEntity.ok(authenticationService.SignUp(signupDTO));

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/onBoardNewDriver/{userId}")
    ResponseEntity<DriverDTO> onBoardNewDriver(@PathVariable Long userId,
                                               @RequestBody onBoardDriverDTO onBoardDriverdto){
        return ResponseEntity.ok(authenticationService.onBoardNewDriver(userId,onBoardDriverdto.getVehicleId()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDTO> refresh(HttpServletRequest request){
        String refreshToken= Arrays.stream(request.getCookies())
                .filter(cookie -> "refreshToken".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElseThrow(()-> new AuthenticationServiceException("Refresh Token Not Found"));

        String accessToken=authenticationService.refreshToken(refreshToken);

        return ResponseEntity.ok(new LoginResponseDTO(accessToken));
    }

}
