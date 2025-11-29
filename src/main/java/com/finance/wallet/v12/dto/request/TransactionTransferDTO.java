package com.finance.wallet.v12.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionTransferDTO(
        @NotBlank
        UUID walletSender,
        @NotBlank
        UUID walletReceiver,
        @Positive
        BigDecimal amount
){}