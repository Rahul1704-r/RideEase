package com.Uber.Project.UberApp.Services;

import com.Uber.Project.UberApp.DTO.DriverDTO;
import com.Uber.Project.UberApp.DTO.RiderDTO;
import com.Uber.Project.UberApp.Entity.Driver;
import com.Uber.Project.UberApp.Entity.Ride;
import com.Uber.Project.UberApp.Entity.Rider;

public interface RatingService {
    DriverDTO rateDriver(Ride ride, Integer rating);

    RiderDTO rateRider(Ride ride, Integer rating);

    void createNewRating(Ride ride);
}
