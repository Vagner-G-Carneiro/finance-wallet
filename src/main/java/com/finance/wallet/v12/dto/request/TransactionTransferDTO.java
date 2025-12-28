package com.finance.wallet.v12.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionTransferDTO(
        @NotBlank(message = "Carteira cededora é obrigatório para a tranferencia!")
        UUID walletSender,
        @NotBlank(message = "Carteira receptora é obrigatório para a tranferencia!")
        UUID walletReceiver,
        @Positive(message = "Valor de depósito precisa ser positivo!")
        @NotBlank(message = "Valor de depósito precisa ser expecificado")
        BigDecimal amount
){}