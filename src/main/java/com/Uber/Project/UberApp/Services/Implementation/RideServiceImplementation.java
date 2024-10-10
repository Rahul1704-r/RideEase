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
        return rideRepositories.findById(rideId).orElseThrow(()->
                new ResourceNotFoundException("Ride Not Found With Id"+rideId));

    }


    @Override
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideStatus.CONFIRMED);
        
        Ride ride= modelMapper.map(rideRequest,Ride.class);
        
        ride.setRideRequestStatus(RideStatus.CONFIRMED);
        
        ride.setDriver(driver);
        
        ride.setOtp(generateRandomOtp());
        
        ride.setId(null);
        
        rideRequestService.update(rideRequest);
        
        return rideRepositories.save(ride);

    }

    @Override
    public Ride UpdateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideRequestStatus(rideStatus);

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
        int otpInt=random.nextInt(10000);

        return String.format("%04d",otpInt);
    }
}
