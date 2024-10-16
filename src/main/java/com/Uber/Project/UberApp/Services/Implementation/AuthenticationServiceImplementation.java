package com.Uber.Project.UberApp.Services.Implementation;

import com.Uber.Project.UberApp.DTO.DriverDTO;
import com.Uber.Project.UberApp.DTO.SignupDTO;
import com.Uber.Project.UberApp.DTO.UserDTO;
import com.Uber.Project.UberApp.Entity.Driver;
import com.Uber.Project.UberApp.Entity.Enum.Role;
import com.Uber.Project.UberApp.Entity.User;
import com.Uber.Project.UberApp.Exceptions.ResourceNotFoundException;
import com.Uber.Project.UberApp.Exceptions.RuntimeConflictException;
import com.Uber.Project.UberApp.Repositories.UserRepositories;
import com.Uber.Project.UberApp.Security.JwtService;
import com.Uber.Project.UberApp.Services.AuthenticationService;
import com.Uber.Project.UberApp.Services.DriverService;
import com.Uber.Project.UberApp.Services.RiderService;
import com.Uber.Project.UberApp.Services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.Uber.Project.UberApp.Entity.Enum.Role.DRIVER;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplementation implements AuthenticationService {

    private final ModelMapper modelMapper;
    private final UserRepositories userRepositories;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;



    @Override
    public String[] Login(String email, String password) {
        String tokens[]=new String[2];
        Authentication authentication=authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email,password)
        );
        User user=(User) authentication.getPrincipal();

        String accessToken=jwtService.generateAccessToken(user);
        String refreshToken=jwtService.generateRefreshToken(user);

        return new String[]{accessToken,refreshToken};

    }

    @Override
    @Transactional 
    public UserDTO SignUp(SignupDTO signupdto) {
        
       User user = userRepositories.findByEmail(signupdto.getEmail()).orElse(null);
       if(user!=null){
           throw new RuntimeConflictException("cannot signup,User already exists");
       }
        User mappeduser=modelMapper.map(signupdto,User.class);
        mappeduser.setRoles(Set.of(Role.RIDER));
        mappeduser.setPassword(passwordEncoder.encode(mappeduser.getPassword()));
        User savedUser=userRepositories.save(mappeduser);
        riderService.createNewRider(savedUser);
        walletService.createNewWallet(savedUser);
        return modelMapper.map(savedUser,UserDTO.class);
    }

    @Override
    public DriverDTO onBoardNewDriver(Long  userId,String vehicleId) {
        User user=userRepositories.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User not Found with id:"+userId));

        if(user.getRoles().contains(DRIVER)){
            throw new RuntimeException("User with id"+userId+"already a driver");
        }

        Driver createDriver=Driver.builder()
                .user(user)
                .rating(0.0)
                .isAvailable(true)
                .VehicleId(vehicleId)
                .build();

        user.getRoles().add(DRIVER);
        userRepositories.save(user);
        Driver savedDriver=driverService.createNewDriver(createDriver);

        return modelMapper.map(savedDriver,DriverDTO.class);


    }

    @Override
    public String refreshToken(String refreshToken) {
        Long userId=jwtService.getUserIdFromToken(refreshToken);
        User user=userRepositories.findById(userId).orElseThrow(()->
                new ResourceNotFoundException("User Not Found with Id"+userId));

        return jwtService.generateAccessToken(user);
    }
}
