package com.finance.wallet.v12.dto.response;

import com.finance.wallet.v12.domain.OperationType;
import com.finance.wallet.v12.domain.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponseDTO(
        UUID id,
        UUID walletReceiver,
        UUID walletSender,
        BigDecimal amount,
        Instant createdAt,
        OperationType operationType
){
    public static TransactionResponseDTO fromEntity (Transaction transaction)
    {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getWalletReceiver().getId(),
                transaction.getWalletSender() != null ?
                        transaction.getWalletSender().getId() : null,
                transaction.getAmount(),
                transaction.getCreatedAt(),
                transaction.getOperationType()
        );
    }
}