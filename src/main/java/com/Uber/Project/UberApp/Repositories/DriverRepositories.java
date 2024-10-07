package com.Uber.Project.UberApp.Repositories;

import com.Uber.Project.UberApp.Entity.Driver;
import com.Uber.Project.UberApp.Entity.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//ST_DISTANCE() uses to calculate distance between 2 points
//ST_DWithin()
// drivers are "d"

@Repository
public interface DriverRepositories extends JpaRepository<Driver, Long> {

    //Takes in the pickUpLocation and returns us the drivers
    @Query(value = "SELECT d.*, ST_Distance(d.current_location , :pickUpLocation) AS distance " +
            "FROM driver  d " +
            "WHERE d.is_available=true AND ST_DWithin( d.current_location , :pickUpLocation , 10000) " +
            "ORDER BY distance " +
             "LIMIT 10 " , nativeQuery = true)

    List<Driver> find10NearestDriver(Point pickUpLocation);


    @Query(value = " SELECT d.*  " +
            "FROM driver d " +
            "WHERE d.is_available = true AND ST_DWithin( d.current_location , :pickUpLocation , 150000) " +
            "ORDER BY d.rating DESC " +
            "LIMIT 10 " , nativeQuery = true)
    List<Driver> findTenNearByTopRatedDrivers(Point pickUpLocation);

    Optional<Driver> findByUser(User user);





}
