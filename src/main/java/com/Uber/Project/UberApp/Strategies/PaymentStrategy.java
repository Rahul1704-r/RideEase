package com.Uber.Project.UberApp.Strategies;

import com.Uber.Project.UberApp.Entity.Payment;

public interface PaymentStrategy {

    Double PLATFORM_COMMISSION=0.3;
    void processPayment(Payment payment);


}
