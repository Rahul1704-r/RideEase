package com.Uber.Project.UberApp.Strategies.Implementation;

import com.Uber.Project.UberApp.Entity.Driver;
import com.Uber.Project.UberApp.Entity.RideRequest;
import com.Uber.Project.UberApp.Repositories.DriverRepositories;
import com.Uber.Project.UberApp.Strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingHighestRatedImplementation implements DriverMatchingStrategy {

    private final DriverRepositories driverRepositories;
    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {

        //returns top High Rated Driver from Database
        return driverRepositories.findTenNearByTopRatedDrivers(rideRequest.getPickUpLocation());
    }
}
