package com.Uber.Project.UberApp.Services.Implementation;

import com.Uber.Project.UberApp.Entity.RideRequest;
import com.Uber.Project.UberApp.Exceptions.ResourceNotFoundException;
import com.Uber.Project.UberApp.Repositories.RideRequestRepositories;
import com.Uber.Project.UberApp.Services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImplementation implements RideRequestService {

    private final RideRequestRepositories rideRequestRepositories;

    @Override
    public RideRequest findRideRequestById(long rideRequestId) {

        //fetching rideRequest By id through query
        return rideRequestRepositories.findById(rideRequestId).orElseThrow(()->
                new ResourceNotFoundException("RideRequest Not Found With Id"+rideRequestId));

    }

    @Override
    public void update(RideRequest rideRequest) {

        //return rideRequest with id
       rideRequestRepositories.findById(rideRequest.getId())
               .orElseThrow(()->new ResourceNotFoundException("RideRequest Not Found With Id"+rideRequest.getId()));

       //save that returned output
       rideRequestRepositories.save(rideRequest);
    }

}
