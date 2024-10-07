package com.Uber.Project.UberApp.Services.Implementation;

import com.Uber.Project.UberApp.Entity.Enum.PaymentStatus;
import com.Uber.Project.UberApp.Entity.Payment;
import com.Uber.Project.UberApp.Entity.Ride;
import com.Uber.Project.UberApp.Exceptions.ResourceNotFoundException;
import com.Uber.Project.UberApp.Repositories.PaymentRepositories;
import com.Uber.Project.UberApp.Services.PaymentService;
import com.Uber.Project.UberApp.Strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImplementation implements PaymentService {

    private final PaymentRepositories paymentRepositories;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride) {
        Payment payment=paymentRepositories.findByRide(ride).orElseThrow(()->
                new ResourceNotFoundException("Payment Not Found with ride id :"+ride.getId()));
        paymentStrategyManager.paymentStrategy(payment.getPaymentMethod()).processPayment(payment);
    }

    @Override
    public Payment createNewPayment(Ride ride) {
        Payment payment=Payment.builder()
                .ride(ride)
                .paymentMethod(ride.getPaymentMethod())
                .amount(ride.getFare())
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        return paymentRepositories.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus status) {
        payment.setPaymentStatus(status);

        paymentRepositories.save(payment);
    }
}
