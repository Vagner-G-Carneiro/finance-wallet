package com.finance.wallet.v12.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record TransactionTransferDTO(
        @NotBlank(message = "Carteira cededora é obrigatório para a tranferencia!")
        UUID walletSender,
        @NotBlank(message = "Carteira receptora é obrigatório para a tranferencia!")
        UUID walletReceiver,
        @NotBlank(message = "Valor de depósito precisa ser expecificado")
        @Digits(integer = 19, fraction = 2)
        @DecimalMin(value = "0.00", inclusive = false, message = "Valor de transação precisa ser maior que zero.")
        BigDecimal amount
){}