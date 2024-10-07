package com.Uber.Project.UberApp.Repositories;

import com.Uber.Project.UberApp.Entity.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RiderRepositories extends JpaRepository<Rider, Long> {
}
