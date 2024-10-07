package com.Uber.Project.UberApp.Repositories;

import com.Uber.Project.UberApp.Entity.Driver;
import com.Uber.Project.UberApp.Entity.Ride;
import com.Uber.Project.UberApp.Entity.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRepositories extends JpaRepository<Ride, Long> {
    Page<Ride> findByRider(Rider rider, Pageable pageRequest);

    Page<Ride> findByDriver(Driver driver, Pageable pageRequest);
}
