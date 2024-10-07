package com.Uber.Project.UberApp.Repositories;

import com.Uber.Project.UberApp.Entity.User;
import com.Uber.Project.UberApp.Entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {


    Optional<Wallet> findByUser(User user);
}
