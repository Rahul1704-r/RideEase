package com.Uber.Project.UberApp.Services.Implementation;

import com.Uber.Project.UberApp.Entity.Driver;
import com.Uber.Project.UberApp.Entity.Enum.RideStatus;
import com.Uber.Project.UberApp.Entity.Ride;
import com.Uber.Project.UberApp.Entity.RideRequest;
import com.Uber.Project.UberApp.Entity.Rider;
import com.Uber.Project.UberApp.Exceptions.ResourceNotFoundException;
import com.Uber.Project.UberApp.Repositories.RideRepositories;
import com.Uber.Project.UberApp.Services.RideRequestService;
import com.Uber.Project.UberApp.Services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImplementation implements RideService {

    private final RideRepositories rideRepositories;
    private final RideRequestService rideRequestService;
    private final ModelMapper modelMapper;

    @Override
    public Ride getRideById(Long rideId) {
        //fetch Ride details from the dataBase through query
        return rideRepositories.findById(rideId).orElseThrow(()->
                new ResourceNotFoundException("Ride Not Found With Id"+rideId));

    }


    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        //set rideRequest status to CONFIRMED
        rideRequest.setRideRequestStatus(RideStatus.CONFIRMED);

        //convert RideDTO to RideEntity
        Ride ride= modelMapper.map(rideRequest,Ride.class);

        //set the rideStatus to CONFIRM
        ride.setRideRequestStatus(RideStatus.CONFIRMED);

        // set the driver
        ride.setDriver(driver);

        //GENERATE OTP using generateRandomOtp method
        ride.setOtp(generateRandomOtp());


        ride.setId(null);

        // update the rideRequest as we have updated the ride status of rideRequest/save's it
        rideRequestService.update(rideRequest);

        //return saved RideEntity
         return rideRepositories.save(ride);

    }

    @Override
    public Ride UpdateRideStatus(Ride ride, RideStatus rideStatus) {
        //update ride request status based on the parameters
        ride.setRideRequestStatus(rideStatus);

        //return the saved ride
        return rideRepositories.save(ride);

    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepositories.findByRider(rider,pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
       return rideRepositories.findByDriver(driver,pageRequest);
    }

    private String generateRandomOtp(){
        Random random = new Random();
        //return number between 0-9999
        int otpInt=random.nextInt(10000);

        // will convert 3digit-2digit-1digit to 4digit
        return String.format("%04d",otpInt);
    }
}
