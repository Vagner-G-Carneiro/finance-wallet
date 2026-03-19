package com.finance.wallet.v12.domain.enums;

public enum BusinessError implements ErrorInfo {

    INVALID_AMOUNT("WLT-1000", "business.error.wallet.invalid-amount"),
    INSUFFICIENT_BALANCE("WLT-1001", "business.error.wallet.insufficient-balance"),
    INVALID_WALLET_STATUS("WLT-1002", "business.error.wallet.invalid-wallet-status"),
    INVALID_DEPOSIT_AMOUNT("WLT-1003", "business.error.wallet.invalid-deposit-amount"),
    WALLET_NOT_FOUND("WLT-1004", "business.error.wallet.not-found"),
    INVALID_WALLET("WLT-1005", "business.error.wallet.invalid-wallet"),


    SELF_TRANSACTION("TSC-1000", "business.error.transaction.self-transaction"),

    MONEY_PARSE_ERROR("MN-1001", "business.error.money.parse-error");

    private final String code;
    private final String message;

    BusinessError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
