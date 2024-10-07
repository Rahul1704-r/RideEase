package com.Uber.Project.UberApp.Repositories;

import com.Uber.Project.UberApp.Entity.Payment;
import com.Uber.Project.UberApp.Entity.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepositories extends JpaRepository<Payment,Long> {
    Optional<Payment> findByRide(Ride ride);
}
