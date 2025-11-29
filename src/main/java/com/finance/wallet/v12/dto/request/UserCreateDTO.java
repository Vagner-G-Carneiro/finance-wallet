package com.finance.wallet.v12.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record UserCreateDTO(
        @NotBlank(message = "Nome é um campo obrigatório.")
        String name,

        @NotBlank(message = "CPF é um campo obrigatório.")
        @CPF(message = "Formato de CPF inválido.")
        String cpf,

        @NotBlank(message = "Email é um campo obrigatório.")
        @Email(message = "Formato de Email inválido.")
        String email,

        @NotBlank(message = "Senha é um campo obrigatório.")
        String password
){}
