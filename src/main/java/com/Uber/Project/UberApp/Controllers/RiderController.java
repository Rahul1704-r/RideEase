package com.Uber.Project.UberApp.Controllers;

import com.Uber.Project.UberApp.DTO.*;
import com.Uber.Project.UberApp.Services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rider")
@RequiredArgsConstructor
@Secured("ROLE_RIDER")//only rider are allowed to call this methods
public class RiderController {
    private final RiderService riderService;

    @PostMapping("/requestRide")
    public ResponseEntity<RideRequestDTO> requestRide(@RequestBody RideRequestDTO rideRequestDTO){
        return ResponseEntity.ok(riderService.requestRide(rideRequestDTO));
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RideDTO> cancelRide(@PathVariable Long rideId){
        return ResponseEntity.ok(riderService.cancelRide(rideId));
    }

    @PostMapping("/rating")
    public ResponseEntity<RiderDTO> ratingDriver(@RequestBody RatingDTO ratingDTO){
        return ResponseEntity.ok(riderService.rateDriver(ratingDTO.getRideId(), ratingDTO.getRating()));
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<RiderDTO> getMyProfile(){
        return ResponseEntity.ok(riderService.getMyProfile());
    }

    @GetMapping("/getMyRides")
    public ResponseEntity<Page<RiderDTO>> getMyRides(@RequestParam(defaultValue = "0")Integer pageOffset,
                                                     @RequestParam(defaultValue = "10" , required = false) Integer PageSize){
        PageRequest pageRequest=PageRequest.of(pageOffset,PageSize);
        return ResponseEntity.ok(riderService.getAllMyRides(pageRequest));
    }


}
