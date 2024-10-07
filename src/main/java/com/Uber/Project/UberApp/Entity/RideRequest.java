package com.Uber.Project.UberApp.Entity;

import com.Uber.Project.UberApp.Entity.Enum.PaymentMethod;
import com.Uber.Project.UberApp.Entity.Enum.RideStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(indexes = {
        @Index(name = "idx_ride_request_rider",columnList = "rider_id")
})

public class RideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "Geometry(Point,4326)")
    private Point PickUpLocation;

    @Column(columnDefinition="Geometry(Point,4326)")
    private Point DropOffLocation;

    @CreationTimestamp
    /*will automatically fill the requested Time
    so u don't have to this*/
    private LocalDateTime RequestTime;

    @ManyToOne(fetch = FetchType.LAZY)
    /* one rider can have multiple
    Ride request*/
    private Rider rider;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private RideStatus rideRequestStatus;

    private double fare;





}
