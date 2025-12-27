package com.finance.wallet.v12.infra.security;

import java.time.Instant;

public record JwtPayload(
        String email,
        Instant iat)
{}
