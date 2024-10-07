package com.Uber.Project.UberApp.DTO;

import com.Uber.Project.UberApp.Entity.Enum.PaymentMethod;
import com.Uber.Project.UberApp.Entity.Enum.RideRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequestDTO {

    private Long id;
    private PointDTO PickUpLocation;
    private PointDTO DropOffLocation;
    private LocalDateTime RequestTime;

    private Double fare;
    private RiderDTO rider;
    private PaymentMethod paymentMethod;
    private RideRequestStatus rideRequestStatus;
}
