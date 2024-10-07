package com.Uber.Project.UberApp.Strategies.Implementation;

import com.Uber.Project.UberApp.Entity.Driver;
import com.Uber.Project.UberApp.Entity.Enum.PaymentStatus;
import com.Uber.Project.UberApp.Entity.Enum.TransactionMethod;
import com.Uber.Project.UberApp.Entity.Payment;
import com.Uber.Project.UberApp.Entity.Wallet;
import com.Uber.Project.UberApp.Repositories.PaymentRepositories;
import com.Uber.Project.UberApp.Services.WalletService;
import com.Uber.Project.UberApp.Strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CODPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepositories paymentRepositories;

    @Override
    public void processPayment(Payment payment) {
        //fetch driver details
        Driver driver=payment.getRide().getDriver();

        //fetch the driver's Wallet details
        Wallet driverWallet=walletService.findByUser(driver.getUser());

        //Calculate the PlatformCommission
        double platformCommission= payment.getAmount()*PLATFORM_COMMISSION;

        //deduct PlatformCommission From Driver's Wallet
        walletService.deductMoneyFromWallet(driver.getUser()
                , platformCommission,null,payment.getRide(), TransactionMethod.RIDE);

        //Update Payment Status to CONFIRM
        payment.setPaymentStatus(PaymentStatus.CONFIRMED);

        //Save the Payment details
        paymentRepositories.save(payment);
    }
}
