package com.finance.wallet.v12.infra.exceptions;

import org.springframework.http.HttpStatus;

public class V12MoneyException extends V12BusinessException {
    public V12MoneyException(String message, HttpStatus status) {
        super(message, "Money Exception", status);
    }
}
