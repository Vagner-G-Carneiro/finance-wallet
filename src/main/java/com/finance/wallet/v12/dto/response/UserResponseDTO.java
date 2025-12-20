package com.finance.wallet.v12.dto.response;

import com.finance.wallet.v12.domain.User;

public record UserResponseDTO(
        String name,
        String email
){
    public static UserResponseDTO fromEntity(User user)
    {
        return new UserResponseDTO(
                user.getName(),
                user.getEmail()
        );
    }
}
