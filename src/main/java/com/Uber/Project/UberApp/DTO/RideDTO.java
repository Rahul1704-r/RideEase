package com.Uber.Project.UberApp.DTO;

import com.Uber.Project.UberApp.Entity.Enum.PaymentMethod;
import com.Uber.Project.UberApp.Entity.Enum.RideStatus;
import lombok.Data;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Data
public class RideDTO {

    private Long id;
    private PointDTO PickUpLocation;
    private PointDTO DropOffLocation;
    private LocalDateTime RequestTime;
    private RiderDTO rider;
    private String otp;
    private DriverDTO driver;
    private PaymentMethod paymentMethod;
    private RideStatus rideRequestStatus;
    private Double fare;
    private LocalDateTime startedTime;
    private LocalDateTime endedTime;
}
