package com.Uber.Project.UberApp.Strategies;

import com.Uber.Project.UberApp.Entity.Driver;
import com.Uber.Project.UberApp.Entity.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {

     List<Driver> findMatchingDriver(RideRequest rideRequest);
}
