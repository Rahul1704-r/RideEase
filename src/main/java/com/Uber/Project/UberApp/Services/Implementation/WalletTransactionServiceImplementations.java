package com.Uber.Project.UberApp.Services.Implementation;

import com.Uber.Project.UberApp.DTO.WalletTransactionDTO;
import com.Uber.Project.UberApp.Entity.WalletTransaction;
import com.Uber.Project.UberApp.Repositories.WalletTransactionsRepositories;
import com.Uber.Project.UberApp.Services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImplementations implements WalletTransactionService {

    private final WalletTransactionsRepositories walletTransactionsRepositories;
    private final ModelMapper mapper;

    @Override
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        //might error
        walletTransactionsRepositories.save(walletTransaction);
    }
}
