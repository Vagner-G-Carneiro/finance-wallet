package com.finance.wallet.v12.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank(message = "Email é um campo obrigatório.")
        @Email(message = "Formato de Email inválido.")
        String email,
        String password){
}
