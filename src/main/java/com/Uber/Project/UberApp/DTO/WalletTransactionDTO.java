package com.Uber.Project.UberApp.DTO;

import com.Uber.Project.UberApp.Entity.Enum.TransactionMethod;
import com.Uber.Project.UberApp.Entity.Enum.TransactionType;
import com.Uber.Project.UberApp.Entity.Ride;
import com.Uber.Project.UberApp.Entity.Wallet;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Builder
public class WalletTransactionDTO {

    private Long id;
    private Double amount;
    private TransactionType transactionType;
    private TransactionMethod transactionMethod;
    private Ride ride;
    private String transactionId;
    private WalletDTO wallet;
    private LocalDateTime timeStamp;

}
