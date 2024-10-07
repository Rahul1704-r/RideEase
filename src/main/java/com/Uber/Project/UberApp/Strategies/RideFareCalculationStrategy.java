package com.Uber.Project.UberApp.Strategies;

import com.Uber.Project.UberApp.Entity.RideRequest;

public interface RideFareCalculationStrategy {

     double RIDE_FARE_MULTIPLIER=10;

    Double calculateFare(RideRequest rideRequest);
}
