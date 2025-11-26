package com.finance.wallet.v12.infra.exceptions;

public class V12WalletException extends V12BusinessException {
    public V12WalletException(String message) {
        super(message, "Wallet-Exception");
    }
}
