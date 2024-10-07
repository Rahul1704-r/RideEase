package com.Uber.Project.UberApp.Services;

import com.Uber.Project.UberApp.Entity.RideRequest;

public interface RideRequestService{

    RideRequest findRideRequestById(long rideRequestId);

    void update(RideRequest rideRequest);
}
