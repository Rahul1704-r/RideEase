package com.Uber.Project.UberApp.Strategies;

import com.Uber.Project.UberApp.Entity.Enum.PaymentMethod;
import com.Uber.Project.UberApp.Strategies.Implementation.CODPaymentStrategy;
import com.Uber.Project.UberApp.Strategies.Implementation.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {
    private final WalletPaymentStrategy walletPaymentStrategy;
    private final CODPaymentStrategy codPaymentStrategy;

    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod){
       return  switch(paymentMethod){
           case WALLET->walletPaymentStrategy;
           case CASH->codPaymentStrategy;
        };
    }
}
