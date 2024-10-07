
package com.Uber.Project.UberApp.Strategies;

import com.Uber.Project.UberApp.Strategies.Implementation.DriverMatchingHighestRatedImplementation;
import com.Uber.Project.UberApp.Strategies.Implementation.DriverMatchingNearestDriverImplementation;
import com.Uber.Project.UberApp.Strategies.Implementation.RideFareCalculationImplementation;
import com.Uber.Project.UberApp.Strategies.Implementation.RideFareSurgePricingStrategyImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor

// it is responsibility of strategy manager class to return any of the strategy
public class RideStrategyManager {

    private final DriverMatchingNearestDriverImplementation driverMatchingStrategy;
    private final DriverMatchingHighestRatedImplementation driverMatchingHighestRatedStrategy;
    private final RideFareCalculationImplementation rideFareCalculationStrategyImplementation;
    private final RideFareSurgePricingStrategyImplementation rideFareSurgePricingStrategyImplementation;


    //Decide which driver Matching Strategy will be used
    public DriverMatchingStrategy getDriverMatchingStrategy(double rating) {
        if(rating>=4.8){
            return driverMatchingHighestRatedStrategy;
        }else{
            return driverMatchingStrategy;
        }
    }


    //will return which Fare Calculation Strategy should be used
    public RideFareCalculationStrategy rideFareCalculationStrategy(){

        //if its peak hours like 6PM - 9PM
        LocalTime surgeStartTime=LocalTime.of(18,0);
        LocalTime surgeEndTime=LocalTime.of(21,0);

        //find current time
        LocalTime currentTime=LocalTime.now();

       //finding if its  peak hour or not
        boolean isSurgeTime=currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        //if peak hours then ask for surge Price
        if(isSurgeTime){
            return rideFareSurgePricingStrategyImplementation;

        //or ask for default price
        }else{
            return rideFareCalculationStrategyImplementation;
        }

    }

}
