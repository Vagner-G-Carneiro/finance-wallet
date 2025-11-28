package com.finance.wallet.v12.infra.exceptions;

import org.springframework.http.HttpStatus;

public class V12WalletException extends V12BusinessException {
    public V12WalletException(String message, HttpStatus httpStatus) {
        super(message, "Wallet-Exception", httpStatus);
    }
}
