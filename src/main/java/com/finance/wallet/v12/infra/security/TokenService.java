package com.finance.wallet.v12.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.finance.wallet.v12.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secretKey;
    public String generateToken(User user)
    {
        System.out.println("\n\nSECRET KEY = " + secretKey);
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withIssuer("v12-finance-wallet")
                .withSubject(user.getId().toString())
                .withIssuedAt(generateExpirationDate())
                .sign(algorithm);
    }

    public String decodeToken(String token)
    {
        try
        {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer("v12-finance-wallet")
                    .acceptExpiresAt(0)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            return "";
        }
    }

    private Instant generateExpirationDate()
    {
        return Instant.now().plus(20, ChronoUnit.MINUTES);
    }
}
