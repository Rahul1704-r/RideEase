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
        //Find Wallet by User
        Wallet wallet=findByUser(user);

        //add Money to the User's Wallet
        wallet.setBalance(wallet.getBalance()+amount);

        //Build transaction object and populate them
        WalletTransaction walletTransaction= WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.CREDIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

        // Create New Wallet Transaction
        walletTransactionService.createNewWalletTransaction(walletTransaction);

        //Save the Wallet details
        return walletRepository.save(wallet);


    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, Double amount, String transactionId,
                                        Ride ride, TransactionMethod transactionMethod) {
        //Find Wallet By USER
        Wallet wallet=findByUser(user);

        //Deduct Money From Wallet
        wallet.setBalance(wallet.getBalance()-amount);

        //Build WalletTransaction and Populate the field
        WalletTransaction walletTransaction= WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .transactionMethod(transactionMethod)
                .amount(amount)
                .build();

        //walletTransactionService.createNewWalletTransaction(walletTransaction);

        wallet.getTransactions().add(walletTransaction);

        //Save Wallet Details
        return walletRepository.save(wallet);
    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        //Find Wallet By WalletId
        return walletRepository.findById(walletId).orElseThrow(()->
                new ResourceNotFoundException("Wallet Not Found with Id :"+walletId));
    }

    @Override
    public Wallet createNewWallet(User user) {
        //Create New Wallet Object
        Wallet wallet =new Wallet();

        //set the User
        wallet.setUser(user);

        //Save Wallet Details
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findByUser(User user) {
        //Find Wallet By User
        return walletRepository.findByUser(user).orElseThrow(()->
                new ResourceNotFoundException("Wallet Not Found with UserId : "+user.getId()));
    }
}
