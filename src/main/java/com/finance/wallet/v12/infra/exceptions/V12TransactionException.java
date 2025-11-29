package com.finance.wallet.v12.infra.exceptions;

import org.springframework.http.HttpStatus;

public class V12TransactionException extends V12BusinessException {
    public V12TransactionException(String message, HttpStatus httpStatus) {
        super(message, "Transaction Exception", httpStatus);
    }
}
