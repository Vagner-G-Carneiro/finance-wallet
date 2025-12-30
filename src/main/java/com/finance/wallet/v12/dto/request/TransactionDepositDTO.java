package com.finance.wallet.v12.dto.request;

import com.finance.wallet.v12.domain.Money;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransactionDepositDTO(
        @NotBlank(message = "Carteira receptora é obrigatório para o depósito!")
        UUID walletReceiver,
        @NotBlank(message = "Valor de depósito precisa ser expecificado")
        @Digits(integer = 19, fraction = 2)
        @DecimalMin(value = "0.00", inclusive = false, message = "Valor de transação precisa ser maior que zero.")
        BigDecimal amount
){}
