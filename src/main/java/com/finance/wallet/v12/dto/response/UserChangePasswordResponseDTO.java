package com.finance.wallet.v12.dto.response;

import java.time.Instant;

public record UserChangePasswordResponseDTO(
        String message,
        Instant now
){}
