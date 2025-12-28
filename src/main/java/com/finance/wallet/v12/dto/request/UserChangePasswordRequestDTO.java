package com.finance.wallet.v12.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserChangePasswordRequestDTO(
        @Email(message = "Formato de email inválido")
        @NotBlank(message = "Campo 'Email' é obrigatório.")
        String email,

        @NotBlank(message = "Campo 'senha' é obrigatório para troca da mesma.")
        String password,

        @NotBlank(message = "Campo 'confirmar senha' é obrigatório para troca da senha.")
        String confirmPassword,

        @NotBlank(message = "Você precisa informar uma nova senha.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$",
        message = "Senha deve conter pelo menos 8 caracteres, letra minuscula, maiuscula e um numero.")
        String newPassword
){}
