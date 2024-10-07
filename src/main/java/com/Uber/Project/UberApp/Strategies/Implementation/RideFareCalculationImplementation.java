package com.Uber.Project.UberApp.Strategies.Implementation;

import com.Uber.Project.UberApp.Entity.RideRequest;
import com.Uber.Project.UberApp.Services.DistanceService;
import com.Uber.Project.UberApp.Strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareCalculationImplementation implements RideFareCalculationStrategy {

    //returns the distance
    private final DistanceService distanceService;
    @Override
    public Double calculateFare(RideRequest rideRequest) {
        double distance=distanceService.getDistance(rideRequest.getPickUpLocation(),
                rideRequest.getDropOffLocation());

        return distance*RIDE_FARE_MULTIPLIER;

    }
}
