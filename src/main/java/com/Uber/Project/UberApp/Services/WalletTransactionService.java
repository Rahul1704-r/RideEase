package com.Uber.Project.UberApp.Services;

import com.Uber.Project.UberApp.DTO.WalletTransactionDTO;
import com.Uber.Project.UberApp.Entity.WalletTransaction;

public interface WalletTransactionService {

    void createNewWalletTransaction(WalletTransaction walletTransaction);
}
