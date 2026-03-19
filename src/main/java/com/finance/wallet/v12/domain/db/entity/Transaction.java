package com.finance.wallet.v12.domain.db.entity;

import com.finance.wallet.v12.domain.Money;
import com.finance.wallet.v12.domain.Wallet;
import com.finance.wallet.v12.domain.enums.OperationType;
import jakarta.persistence.*;
import lombok.*;


@Table(name = "transactions")
@Builder(toBuilder = true, builderClassName = "Builder")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends AbstractEntity{


    @ManyToOne
    @JoinColumn(name = "wallet_sender_id")
    private Wallet source;

    @ManyToOne
    @JoinColumn(name = "wallet_receiver_id", nullable = false)
    private Wallet target;


    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column (
                    name = "amount"
            )
    )
    @lombok.Builder.Default
    private Money amount = Money.zero();

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;


//    private static void  validateAmount(Money amount) {
//        if(!amount.isGreaterThan(Money.zero())){
//            throw V12WalletException.businessRule("Impossível existir uma transação com saldo menor ou igual a zero.");
//        }
//    }
//
//    private static void validateTransfer(Wallet walletSender, Wallet walletReceiver)
//    {
//        Wallet.validateWallet(walletSender, walletReceiver);
//        if(walletSender.getId().equals(walletReceiver.getId()))
//        {
//            throw V12TransactionException.businessRule("Você não pode realizar tranferencias de uma carteira para ela mesma!");
//        }
//    }
//
//    private static void transfer(Wallet walletSender, Wallet walletReceiver, Money amount)
//    {
//        validateAmount(amount);
//        validateTransfer(walletSender, walletReceiver);
//        walletSender.withdraw(amount);
//        walletReceiver.deposit(amount);
//    }
//
//    public static Transaction createTransfer(Wallet walletSender, Wallet walletReceiver, Money amount) {
//        transfer(walletSender, walletReceiver, amount);
//        Transaction transaction = new Transaction();
//        transaction.walletSender = walletSender;
//        transaction.walletReceiver = walletReceiver;
//        transaction.amount = amount;
//        transaction.createdAt = Instant.now();
//        transaction.operationType = OperationType.TRANSFER;
//        return transaction;
//    }
//
//    public static Transaction createDeposit(Wallet walletReceiver, Money amount) {
//        validateAmount(amount);
//        Transaction transaction = new Transaction();
//        transaction.walletSender = null;
//        transaction.walletReceiver = walletReceiver;
//        transaction.amount = amount;
//        transaction.createdAt = Instant.now();
//        transaction.operationType = OperationType.DEPOSIT;
//        return transaction;
//    }
}
