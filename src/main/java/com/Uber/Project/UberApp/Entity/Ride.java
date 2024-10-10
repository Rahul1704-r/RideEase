package com.Uber.Project.UberApp.Entity;

import com.Uber.Project.UberApp.Entity.Enum.PaymentMethod;
import com.Uber.Project.UberApp.Entity.Enum.RideRequestStatus;
import com.Uber.Project.UberApp.Entity.Enum.RideStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(indexes = {
        @Index(name="idx_ride_rider", columnList = "rider_id"),
        @Index(name = "idx_ride_driver", columnList = "driver_id")
})
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "Geometry(Point,4326)")
    private Point PickUpLocation;

    @Column(columnDefinition="Geometry(Point,4326)")
    private Point DropOffLocation;

    @CreationTimestamp
    private LocalDateTime CreatedTime;

    @ManyToOne(fetch = FetchType.LAZY)
    private Driver driver;


    @ManyToOne(fetch = FetchType.LAZY)
    private Rider rider;

    private String otp;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private RideStatus rideRequestStatus;

    private double fare;

    private LocalDateTime StartedAt;
    private LocalDateTime EndedAt;
}
