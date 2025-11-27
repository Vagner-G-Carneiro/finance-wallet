package com.finance.wallet.v12.infra.exceptions;

public class V12TransactionException extends V12BusinessException {
    public V12TransactionException(String message) {
        super(message, "Transaction Exception");
    }
}
