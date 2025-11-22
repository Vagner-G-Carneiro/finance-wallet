package com.finance.wallet.v12.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDepositDTO(
        @NotBlank
        UUID walletReceiver,
        @NotBlank
        @Positive(message = "Valor de depósito precisa ser sempre maior que 0.")
        BigDecimal amount
){}
