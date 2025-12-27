package com.finance.wallet.v12.infra.exceptions;

import org.springframework.http.HttpStatus;

public class V12SecurityException extends V12BusinessException{
    public V12SecurityException(String message, HttpStatus httpStatus) {
        super(message, "Security Exception", httpStatus);
    }

    public static V12SecurityException invalidToken(String message)
    {
        return new V12SecurityException(message, HttpStatus.UNAUTHORIZED);
    }
}
