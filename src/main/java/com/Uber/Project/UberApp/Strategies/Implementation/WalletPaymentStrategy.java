package com.Uber.Project.UberApp.Strategies.Implementation;

import com.Uber.Project.UberApp.Entity.Driver;
import com.Uber.Project.UberApp.Entity.Enum.PaymentStatus;
import com.Uber.Project.UberApp.Entity.Enum.TransactionMethod;
import com.Uber.Project.UberApp.Entity.Payment;
import com.Uber.Project.UberApp.Entity.Rider;
import com.Uber.Project.UberApp.Repositories.PaymentRepositories;
import com.Uber.Project.UberApp.Services.PaymentService;
import com.Uber.Project.UberApp.Services.WalletService;
import com.Uber.Project.UberApp.Strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//Rider had 232, Driver Had 500
//Ride cost is 100, Commission =30
//Rider=232-100=132
//driver=500*(100-30)=570

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy  implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepositories paymentRepositories;

    @Override
    @Transactional
    public void processPayment(Payment payment) {
        //fetch driver details
        Driver driver=payment.getRide().getDriver();

        //fetch rider details
        Rider rider=payment.getRide().getRider();

        //deduct Money From riderWallet
        walletService.deductMoneyFromWallet(rider.getUser(),
                payment.getAmount(), null,payment.getRide(), TransactionMethod.RIDE);

        //find the Driver's Cut from the Total Amount
        Double driverCut= payment.getAmount()*(1-PLATFORM_COMMISSION);

        //Add that DriverCut to Driver's Wallet
        walletService.addMoneyToWallet(driver.getUser(),
                driverCut,null,payment.getRide(),TransactionMethod.RIDE);

        //update payment Status to CONFIRM
        payment.setPaymentStatus(PaymentStatus.CONFIRMED);

        //Save the payment details
        paymentRepositories.save(payment);

    }
}
