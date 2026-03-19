package com.finance.wallet.v12.dto.response;


import com.finance.wallet.v12.domain.db.entity.Transaction;
import com.finance.wallet.v12.domain.enums.OperationType;

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
                transaction.getTarget().getId(),
                transaction.getSource() != null ?
                        transaction.getSource().getId() : null,
                transaction.getAmount().toBigDecimal(),
                transaction.getCreatedAt(),
                transaction.getOperationType()
        );
    }
}