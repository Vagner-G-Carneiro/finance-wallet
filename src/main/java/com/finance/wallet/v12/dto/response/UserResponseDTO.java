package com.finance.wallet.v12.dto.response;

import com.finance.wallet.v12.domain.User;

import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String nome,
        String cpf,
        String email
){
    public static UserResponseDTO fromEntity(User user)
    {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getCpf(),
                user.getEmail()
        );
    }
}
