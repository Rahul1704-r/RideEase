package com.Uber.Project.UberApp.Services;

import com.Uber.Project.UberApp.DTO.RideDTO;
import com.Uber.Project.UberApp.DTO.RideRequestDTO;
import com.Uber.Project.UberApp.DTO.RiderDTO;
import com.Uber.Project.UberApp.Entity.Rider;
import com.Uber.Project.UberApp.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RiderService {

    RideRequestDTO requestRide(RideRequestDTO rideRequestDTO);

    RideDTO cancelRide(Long rideId);


    RiderDTO rateDriver(Long rideId, Integer rating);

    RiderDTO getMyProfile();

    Page<RiderDTO> getAllMyRides(PageRequest pageRequest);

    Rider createNewRider(User user); //Internal Methods

    Rider getCurrentRider(); // Internal Methods
}
