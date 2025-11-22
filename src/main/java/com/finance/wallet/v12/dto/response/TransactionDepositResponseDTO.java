package com.finance.wallet.v12.dto.response;

import com.finance.wallet.v12.domain.OperationType;
import com.finance.wallet.v12.domain.Transaction;
import com.finance.wallet.v12.dto.request.TransactionDepositDTO;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDepositResponseDTO(
        UUID id,
        UUID walletReceiver,
        BigDecimal amount,
        LocalDateTime createdAt,
        OperationType operationType
){
    public static TransactionDepositResponseDTO fromEntity (Transaction transaction)
    {
        return new TransactionDepositResponseDTO(
                transaction.getId(),
                transaction.getWalletReceiver().getId(),
                transaction.getAmount(),
                transaction.getCreatedAt(),
                transaction.getOperationType()
        );
    }
}
