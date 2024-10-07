package com.Uber.Project.UberApp.Services;

import com.Uber.Project.UberApp.Entity.Enum.PaymentStatus;
import com.Uber.Project.UberApp.Entity.Payment;
import com.Uber.Project.UberApp.Entity.Ride;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus status);
}
