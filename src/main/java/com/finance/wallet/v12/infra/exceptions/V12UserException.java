package com.finance.wallet.v12.infra.exceptions;

import org.springframework.http.HttpStatus;

public class V12UserException extends V12BusinessException {
    public V12UserException(String message, HttpStatus httpStatus) {
        super(message, "User-Exception", httpStatus);
    }
}
