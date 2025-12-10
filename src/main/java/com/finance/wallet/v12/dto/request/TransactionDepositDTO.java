package com.finance.wallet.v12.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDepositDTO(
        @NotBlank(message = "Carteira receptora é obrigatório para o depósito!")
        UUID walletReceiver,
        @NotBlank(message = "Valor de depósito precisa ser expecificado")
        @Positive(message = "Valor de depósito precisa ser sempre maior que 0.")
        BigDecimal amount
){}
