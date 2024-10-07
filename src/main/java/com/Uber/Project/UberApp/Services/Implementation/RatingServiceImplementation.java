package com.Uber.Project.UberApp.Services.Implementation;


import com.Uber.Project.UberApp.DTO.DriverDTO;
import com.Uber.Project.UberApp.DTO.RiderDTO;
import com.Uber.Project.UberApp.Entity.Driver;
import com.Uber.Project.UberApp.Entity.Rating;
import com.Uber.Project.UberApp.Entity.Ride;
import com.Uber.Project.UberApp.Entity.Rider;
import com.Uber.Project.UberApp.Exceptions.ResourceNotFoundException;
import com.Uber.Project.UberApp.Exceptions.RuntimeConflictException;
import com.Uber.Project.UberApp.Repositories.DriverRepositories;
import com.Uber.Project.UberApp.Repositories.RatingRepositories;
import com.Uber.Project.UberApp.Repositories.RideRepositories;
import com.Uber.Project.UberApp.Repositories.RiderRepositories;
import com.Uber.Project.UberApp.Services.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImplementation  implements RatingService {

    private final RatingRepositories ratingRepositories;
    private final DriverRepositories driverRepositories;
    private final RiderRepositories riderRepositories;
    private final ModelMapper modelMapper;

    @Override
    public DriverDTO rateDriver(Ride ride, Integer rating) {
        Driver driver=ride.getDriver();
        Rating ratingObj=ratingRepositories.findByRide(ride).orElseThrow(()->
                new ResourceNotFoundException("Rating Not Found With id:"+ride.getId()));

        if(ratingObj.getDriverRating()!=null){
            throw new RuntimeConflictException("Driver has already been rated,cannot rate again");
        }

        ratingObj.setDriverRating(rating);
        ratingRepositories.save(ratingObj);

        Double newRating=ratingRepositories.findByDriver(driver)
                .stream().mapToDouble(Rating::getDriverRating)
                .average()
                .orElse(0.0);

        driver.setRating(newRating);

        Driver savedDriver=driverRepositories.save(driver);
        return modelMapper.map(savedDriver, DriverDTO.class);



    }

    @Override
    public RiderDTO rateRider(Ride ride, Integer rating) {
        Rider rider=ride.getRider();
        Rating ratingObj=ratingRepositories.findByRide(ride).orElseThrow(()->
                new ResourceNotFoundException("Rating Not Found With id:"+ride.getId()));

        if(ratingObj.getRiderRating()!=null){
            throw new RuntimeConflictException("Rider has already been rated,cannot rate again");
        }

        ratingObj.setDriverRating(rating);

        ratingRepositories.save(ratingObj);

        Double newRating=ratingRepositories.findByRider(rider)
                .stream().mapToDouble(Rating::getDriverRating)
                .average()
                .orElse(0.0);

        rider.setRating(newRating);

        Rider savedRider=riderRepositories.save(rider);
        return modelMapper.map(savedRider, RiderDTO.class);

    }

    @Override
    public void createNewRating(Ride ride) {
        Rating rating=Rating.builder()
                .rider(ride.getRider())
                .driver(ride.getDriver())
                .ride(ride)
                .build();

        ratingRepositories.save(rating);
    }
}
