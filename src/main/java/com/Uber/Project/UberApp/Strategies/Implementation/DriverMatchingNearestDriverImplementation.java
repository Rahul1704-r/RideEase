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
public class DriverMatchingNearestDriverImplementation implements DriverMatchingStrategy {

    private final DriverRepositories driverRepositories;
    @Override
    public List<Driver> findMatchingDriver(RideRequest rideRequest) {

        //returns top10 Nearest Driver By Running Query From DataBase
        return driverRepositories.find10NearestDriver(rideRequest.getPickUpLocation());
    }
}
