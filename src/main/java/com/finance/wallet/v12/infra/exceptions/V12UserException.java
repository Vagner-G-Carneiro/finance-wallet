package com.finance.wallet.v12.infra.exceptions;

public class V12UserException extends V12BusinessException {
    public V12UserException(String message) {
        super(message, "User-Exception");
    }
}
