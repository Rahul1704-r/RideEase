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

        //will fetch rideRequest details from database by rideRequest id
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);

        //if RideRequest is pending only then we will go further else throw exception
        if(!rideRequest.getRideRequestStatus().equals(RideStatus.PENDING)){
            throw new RuntimeException("RideRequest cannot be accepted , status is"+rideRequest.getRideRequestStatus());
        }

        //get the current Driver
        Driver currentDriver = getCurrentDriver();

        //check if the current Driver is Available or not, if not throw exception
        if(!currentDriver.getIsAvailable()){
            throw new RuntimeException("Driver cannot Accept Due to Unavailability");
        }

        //driver have accepted the ride so he cannot accept another ride so mark this driver unavailable
       Driver savedDriver=updateDriverAvailability(currentDriver,false);


        // till now ride have been accepted so Create New Ride
        Ride ride = rideService.createNewRide(rideRequest,savedDriver);

        // convert rideEntity to RideDTO and return
        return modelMapper.map(ride,RideDTO.class);
    }

    @Override
    public RideDTO cancelRide(Long rideId) {
        // fetch ride details
        Ride ride=rideService.getRideById(rideId);

        //fetch driver details
        Driver currentDriver=getCurrentDriver();

        //check if the driver is cancelling the ride
        if(!ride.getDriver().equals(currentDriver)){
            throw new RuntimeException("");
        }

        //check the ride status as ride can only be cancelled only if it was confirmed
        if(!ride.getRideRequestStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("");
        }

        //update the rides status to cancelled
        rideService.UpdateRideStatus(ride,RideStatus.CANCELLED);

        // since the driver has cancelled the ride so make the driver available
        updateDriverAvailability(currentDriver,true);

        //convert ride entity ro rideDTO and return it
        return modelMapper.map(ride,RideDTO.class);

    }

    @Override
    public RideDTO startRide(Long rideID , String otp) {

        //return the ride by id
        Ride ride = rideService.getRideById(rideID);

        //fetch currentDriver
        Driver driver=getCurrentDriver();

        //check if driver is matched to riders driver
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start ride as not accept ride earlier");
        }

        //check if rideRequest status is CONFIRMED
        if(!ride.getRideRequestStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride Status not confirmed hence cannot be started"+" "+ride.getRideRequestStatus());
        }

        //check the OTP
        if(!otp.equals(ride.getOtp())){
            throw new RuntimeException("Otp is not valid"+" "+ride.getOtp());

        }

        //set  start time
        ride.setStartedAt(LocalDateTime.now());

        //set the Ride status to ONGOING as ride has been started
        Ride savedRide= rideService.UpdateRideStatus(ride,RideStatus.ONGOING);

        //Create new Payment
        paymentService.createNewPayment(savedRide);

        //create a rating object
        ratingService.createNewRating(savedRide);

        //convert savedRideEntity to riderDTO and return
        return modelMapper.map(savedRide,RideDTO.class);

    }

    @Override
    public RideDTO endRide(Long rideID) {
        //return the ride by id
        Ride ride = rideService.getRideById(rideID);

        //fetch currentDriver
        Driver driver=getCurrentDriver();

        //check if driver is matched to riders driver
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start ride as not accept ride earlier");
        }

        //check if rideRequest status is ONGOING
        if(!ride.getRideRequestStatus().equals(RideStatus.ONGOING)){
            throw new RuntimeException("Ride Status is not ONGOING hence cannot be ended"+" "+ride.getRideRequestStatus());
        }

        //Set the endTime
        ride.setEndedAt(LocalDateTime.now());

        //update RideStatus to ENDED
        Ride savedRide=rideService.UpdateRideStatus(ride,RideStatus.ENDED);

        //update Driver's availability to true
        updateDriverAvailability(driver,true);

        //process the Payment
        paymentService.processPayment(ride);

        //return RiderDTO
        return modelMapper.map(savedRide,RideDTO.class);

    }

    @Override
    public RiderDTO rateRider(Long rideId, Integer rating) {
        Ride ride= rideService.getRideById(rideId);
        Driver driver=getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver is Not the Owner Of this Ride ");
        }

        //check if rideRequest status is ONGOING
        if(!ride.getRideRequestStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Ride Status is not ENDED hence cannot Start Rating"+" "+ride.getRideRequestStatus());
        }

        return ratingService.rateRider(ride,rating);

    }

    @Override
    public DriverDTO getMyProfile() {

        //get the current driver details
        Driver currentDriver=getCurrentDriver();

        //convert currentDriver entity ti driverDTO and return
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
        //Set the Driver Availability
        driver.setIsAvailable(available);

        //Save the changes
        return driverRepositories.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepositories.save(driver);
    }
}
