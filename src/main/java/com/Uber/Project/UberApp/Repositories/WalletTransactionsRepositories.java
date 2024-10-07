package com.Uber.Project.UberApp.Repositories;

import com.Uber.Project.UberApp.Entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionsRepositories extends JpaRepository<WalletTransaction,Long> {
}
