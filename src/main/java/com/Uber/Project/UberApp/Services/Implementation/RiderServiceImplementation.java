package com.Uber.Project.UberApp.Services.Implementation;

import com.Uber.Project.UberApp.DTO.RideDTO;
import com.Uber.Project.UberApp.DTO.RideRequestDTO;
import com.Uber.Project.UberApp.DTO.RiderDTO;
import com.Uber.Project.UberApp.Entity.*;
import com.Uber.Project.UberApp.Entity.Enum.RideStatus;
import com.Uber.Project.UberApp.Exceptions.ResourceNotFoundException;
import com.Uber.Project.UberApp.Repositories.RideRequestRepositories;
import com.Uber.Project.UberApp.Repositories.RiderRepositories;
import com.Uber.Project.UberApp.Services.DriverService;
import com.Uber.Project.UberApp.Services.RatingService;
import com.Uber.Project.UberApp.Services.RideService;
import com.Uber.Project.UberApp.Services.RiderService;
import com.Uber.Project.UberApp.Strategies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiderServiceImplementation implements RiderService {

    private final ModelMapper modelMapper;
    private final RideStrategyManager rideStrategyManager;
    private final RideRequestRepositories rideRequestRepositories;
    private final RiderRepositories riderRepositories;
    private final RideService rideService;
    private final DriverService driverService;
    private final RatingService ratingService;

    @Override
    public RideRequestDTO requestRide(RideRequestDTO rideRequestDTO) {

        //just for now later will remove
        Rider rider = getCurrentRider();

        //took rideRequestDTO and converted into rideRequestEntity
        RideRequest rideRequest = modelMapper.map(rideRequestDTO, RideRequest.class);

        //setRideRequestStatus to PENDING
        rideRequest.setRideRequestStatus(RideStatus.PENDING);

        //get the role
        rideRequest.setRider(rider);

        /*will calculate Fare for this we used one of the Strategy,
        we are passing whole rideRequest because while calculating fare
         we required pickup and drop off location to get distance
         */
        Double fare=rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);

        //set the fare in my rideRequest Fare
        rideRequest.setFare(fare);

        //then save the rideRequest inside my dataBase
        RideRequest saveRideRequest=rideRequestRepositories.save(rideRequest);

        // will broadcast message to driver its there will to accept or not
        List<Driver> drivers = rideStrategyManager
                .getDriverMatchingStrategy(rider.getRating()).findMatchingDriver(rideRequest);

        // TODO-send notification to all the driver about this ride request

        //return rideRequestDTO
        return modelMapper.map(saveRideRequest, RideRequestDTO.class);
    }

    @Override
    public RideDTO cancelRide(Long rideId) {
        //get the current rider
        Rider rider=getCurrentRider();

        //fetch the ride details
        Ride ride=rideService.getRideById(rideId);

        //check if the rider is the person who is cancelling the ride
        if(!rider.equals(ride.getRider())){
            throw new RuntimeException("");
        }

        //ride can only be cancelled only if ride is confirmed
        if(!ride.getRideRequestStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("");
        }

        //update the ride status to cancelled and save it
        Ride savedRide=rideService.UpdateRideStatus(ride,RideStatus.CANCELLED);

        //update driver availability to true
        driverService.updateDriverAvailability(ride.getDriver(),true);

        //convert savedRideEntity to rideDTO and return
        return modelMapper.map(savedRide,RideDTO.class);
    }

    @Override
    public RiderDTO rateDriver(Long rideId, Integer rating) {

        Ride ride= rideService.getRideById(rideId);
        Rider rider=getCurrentRider();


        if(!rider.equals(ride.getRider())){
            throw new RuntimeException("Rider is Not the Owner Of this Ride ");
        }

        //check if rideRequest status is ONGOING
        if(!ride.getRideRequestStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Ride Status is not ENDED hence cannot Start Rating"+" "+ride.getRideRequestStatus());
        }

        return ratingService.rateRider(ride,rating);
    }

    @Override
    public RiderDTO getMyProfile() {
        // fetch currentRider details
        Rider currentRider=getCurrentRider();

        // convert currentRider to rideDTO and return
        return modelMapper.map(currentRider,RiderDTO.class);
    }

    @Override
    public Page<RiderDTO> getAllMyRides(PageRequest pageRequest) {
       Rider currentRider=getCurrentRider();

       return rideService.getAllRidesOfRider(currentRider,pageRequest).map(
               ride-> modelMapper.map(ride,RiderDTO.class)
       );
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider= Rider
                .builder()
                .user(user)
                .rating(0.0)
                .build();

        return riderRepositories.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
        User user=(User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return riderRepositories.findById(user.getId()).orElseThrow(()->
                new ResourceNotFoundException("Rider Not Found With Id"+user.getId()));
    }
}
