package com.Uber.Project.UberApp.Services.Implementation;

import com.Uber.Project.UberApp.DTO.DriverDTO;
import com.Uber.Project.UberApp.DTO.RideDTO;
import com.Uber.Project.UberApp.DTO.RiderDTO;
import com.Uber.Project.UberApp.Entity.Driver;
import com.Uber.Project.UberApp.Entity.Enum.RideStatus;
import com.Uber.Project.UberApp.Entity.Ride;
import com.Uber.Project.UberApp.Entity.RideRequest;
import com.Uber.Project.UberApp.Entity.User;
import com.Uber.Project.UberApp.Exceptions.ResourceNotFoundException;
import com.Uber.Project.UberApp.Repositories.DriverRepositories;
import com.Uber.Project.UberApp.Services.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverServiceImplementation implements DriverService {

    private final RideRequestService rideRequestService;
    private final DriverRepositories driverRepositories;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;

    @Override
    @Transactional
    public RideDTO acceptRide(Long rideRequestId) {        
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);
        
        if(!rideRequest.getRideRequestStatus().equals(RideStatus.PENDING)){
            throw new RuntimeException("RideRequest cannot be accepted , status is"+rideRequest.getRideRequestStatus());
        }

        Driver currentDriver = getCurrentDriver();
        if(!currentDriver.getIsAvailable()){
            throw new RuntimeException("Driver cannot Accept Due to Unavailability");
        }
       Driver savedDriver=updateDriverAvailability(currentDriver,false);

        Ride ride = rideService.createNewRide(rideRequest,savedDriver);
        return modelMapper.map(ride,RideDTO.class);
    }

    @Override
    public RideDTO cancelRide(Long rideId) {
        Ride ride=rideService.getRideById(rideId);
        Driver currentDriver=getCurrentDriver();
        if(!ride.getDriver().equals(currentDriver)){
            throw new RuntimeException("");
        }
        if(!ride.getRideRequestStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("");
        }
        rideService.UpdateRideStatus(ride,RideStatus.CANCELLED);
        updateDriverAvailability(currentDriver,true);
        return modelMapper.map(ride,RideDTO.class);

    }

    @Override
    public RideDTO startRide(Long rideID , String otp) {

        Ride ride = rideService.getRideById(rideID);

        Driver driver=getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start ride as not accept ride earlier");
        }

        if(!ride.getRideRequestStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride Status not confirmed hence cannot be started"+" "+ride.getRideRequestStatus());
        }

        if(!otp.equals(ride.getOtp())){
            throw new RuntimeException("Otp is not valid"+" "+ride.getOtp());

        }

        ride.setStartedAt(LocalDateTime.now());

        Ride savedRide= rideService.UpdateRideStatus(ride,RideStatus.ONGOING);

        paymentService.createNewPayment(savedRide);

        ratingService.createNewRating(savedRide);

        return modelMapper.map(savedRide,RideDTO.class);

    }

    @Override
    public RideDTO endRide(Long rideID) {
        Ride ride = rideService.getRideById(rideID);
        Driver driver=getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start ride as not accept ride earlier");
        }
        if(!ride.getRideRequestStatus().equals(RideStatus.ONGOING)){
            throw new RuntimeException("Ride Status is not ONGOING hence cannot be ended"+" "+ride.getRideRequestStatus());
        }
        ride.setEndedAt(LocalDateTime.now());
        Ride savedRide=rideService.UpdateRideStatus(ride,RideStatus.ENDED);
        updateDriverAvailability(driver,true);
        paymentService.processPayment(ride);
        return modelMapper.map(savedRide,RideDTO.class);

    }

    @Override
    public RiderDTO rateRider(Long rideId, Integer rating) {
        Ride ride= rideService.getRideById(rideId);
        Driver driver=getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver is Not the Owner Of this Ride ");
        }
        if(!ride.getRideRequestStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Ride Status is not ENDED hence cannot Start Rating"+" "+ride.getRideRequestStatus());
        }
        return ratingService.rateRider(ride,rating);

    }

    @Override
    public DriverDTO getMyProfile() {
        Driver currentDriver=getCurrentDriver();
        return modelMapper.map(currentDriver,DriverDTO.class);

    }

    @Override
    public Page<RideDTO> getAllMyRides(PageRequest pageRequest) {

        Driver currentDriver=getCurrentDriver();

        return rideService.getAllRidesOfDriver(currentDriver,pageRequest).map(
                ride -> modelMapper.map(ride,RideDTO.class)
        );
    }

    @Override
    public Driver getCurrentDriver() {
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return driverRepositories.findByUser(user).orElseThrow(()->
                new ResourceNotFoundException("CurrentDriver Not Found "+user.getId()));

    }

    @Override
    public Driver updateDriverAvailability(Driver driver, boolean available) {
        driver.setIsAvailable(available);
        return driverRepositories.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepositories.save(driver);
    }
}
