package com.Uber.Project.UberApp.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_driver_vehicle_id", columnList = "vehicleId")
})
public class Driver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    /* means table rider will have a field called user_id and
     user_id will be the foreign key of user
     */
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double rating;

    private Boolean isAvailable;

    private String VehicleId;

    @Column(columnDefinition = "Geometry(Point,4326)")
    /* "Geometry(Point,4326)" - Type Geometry Point 4326
    4326 is for dealing with Earth geometry
     */
    private Point currentLocation;
}
