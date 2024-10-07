package com.Uber.Project.UberApp.Repositories;

import com.Uber.Project.UberApp.Entity.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRequestRepositories extends JpaRepository<RideRequest,Long> {
}
