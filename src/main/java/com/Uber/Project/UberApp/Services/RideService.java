package com.Uber.Project.UberApp.Services;

import com.Uber.Project.UberApp.DTO.RideRequestDTO;
import com.Uber.Project.UberApp.Entity.Driver;
import com.Uber.Project.UberApp.Entity.Enum.RideStatus;
import com.Uber.Project.UberApp.Entity.Ride;
import com.Uber.Project.UberApp.Entity.RideRequest;
import com.Uber.Project.UberApp.Entity.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService {

    Ride getRideById(Long rideId);

    Ride createNewRide(RideRequest rideRequest, Driver driver);

    Ride UpdateRideStatus(Ride rider , RideStatus rideStatus);

    Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest);

    Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest);
}
