package com.finance.wallet.v12.domain;

import com.finance.wallet.v12.infra.exceptions.V12TransactionException;
import com.finance.wallet.v12.infra.exceptions.V12WalletException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "transactions")
@Table(name = "transactions")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "wallet_sender_id")
    private Wallet walletSender;

    @ManyToOne
    @JoinColumn(name = "wallet_receiver_id", nullable = false)
    private Wallet walletReceiver;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column (
                    name = "amount"
            )
    )
    private Money amount = Money.zero();

    @Column(name = "created_at")
    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;

    private static void  validateAmount(Wallet walletSender, Wallet walletReceiver, Money amount) {
        if(!amount.isGreaterThan(Money.zero())){
            throw V12WalletException.businessRule("Impossível existir uma transação com saldo menor ou igual a zero.");
        }

        if(walletSender != null && walletSender.getId().equals(walletReceiver.getId())) {
            throw V12TransactionException.businessRule("Uma carteira não pode fazer transação para sí mesma.");
        }
    }

    public static Transaction createTransfer(Wallet walletSender, Wallet walletReceiver, Money amount) {
        validateAmount(walletSender, walletReceiver, amount);
        Transaction transaction = new Transaction();
        transaction.walletSender = walletSender;
        transaction.walletReceiver = walletReceiver;
        transaction.amount = amount;
        transaction.createdAt = Instant.now();
        transaction.operationType = OperationType.TRANSFER;
        return transaction;
    }

    public static Transaction createDeposit(Wallet walletReceiver, Money amount) {
        validateAmount(null, walletReceiver, amount);
        Transaction transaction = new Transaction();
        transaction.walletSender = null;
        transaction.walletReceiver = walletReceiver;
        transaction.amount = amount;
        transaction.createdAt = Instant.now();
        transaction.operationType = OperationType.DEPOSIT;
        return transaction;
    }

}
