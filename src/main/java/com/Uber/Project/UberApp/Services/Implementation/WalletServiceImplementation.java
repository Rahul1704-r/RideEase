package com.Uber.Project.UberApp.Services.Implementation;

import com.Uber.Project.UberApp.DTO.RideDTO;
import com.Uber.Project.UberApp.DTO.WalletTransactionDTO;
import com.Uber.Project.UberApp.Entity.Enum.TransactionMethod;
import com.Uber.Project.UberApp.Entity.Enum.TransactionType;
import com.Uber.Project.UberApp.Entity.Ride;
import com.Uber.Project.UberApp.Entity.User;
import com.Uber.Project.UberApp.Entity.Wallet;
import com.Uber.Project.UberApp.Entity.WalletTransaction;
import com.Uber.Project.UberApp.Exceptions.ResourceNotFoundException;
import com.Uber.Project.UberApp.Repositories.WalletRepository;
import com.Uber.Project.UberApp.Services.WalletService;
import com.Uber.Project.UberApp.Services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletServiceImplementation implements WalletService {

    private final WalletRepository walletRepository;
    private final ModelMapper mapper;
    private final WalletTransactionService walletTransactionService;


    @Override
    public Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet=findByUser(user);

        wallet.setBalance(wallet.getBalance()+amount);

        WalletTransaction walletTransaction= WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.CREDIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

        walletTransactionService.createNewWalletTransaction(walletTransaction);

        return walletRepository.save(wallet);


    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, Double amount, String transactionId,
                                        Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet=findByUser(user);

        wallet.setBalance(wallet.getBalance()-amount);

        WalletTransaction walletTransaction= WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();


        wallet.getTransactions().add(walletTransaction);

        return walletRepository.save(wallet);
    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId).orElseThrow(()->
                new ResourceNotFoundException("Wallet Not Found with Id :"+walletId));
    }

    @Override
    public Wallet createNewWallet(User user) {
        Wallet wallet =new Wallet();

        wallet.setUser(user);

        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user).orElseThrow(()->
                new ResourceNotFoundException("Wallet Not Found with UserId : "+user.getId()));
    }
}
