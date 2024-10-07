package com.Uber.Project.UberApp.Services;

import com.Uber.Project.UberApp.DTO.DriverDTO;
import com.Uber.Project.UberApp.DTO.RideDTO;
import com.Uber.Project.UberApp.DTO.RiderDTO;
import com.Uber.Project.UberApp.Entity.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


public interface DriverService {

    RideDTO acceptRide(Long rideRequestId);

    RideDTO cancelRide(Long rideId);

    RideDTO startRide(Long rideID , String otp);

    RideDTO endRide(Long rideID);

    RiderDTO rateRider(Long rideId, Integer rating);

    DriverDTO getMyProfile();

    Page<RideDTO> getAllMyRides(PageRequest pageRequest);

    Driver getCurrentDriver();

    Driver updateDriverAvailability (Driver driver, boolean available);

    Driver createNewDriver(Driver driver);
}
