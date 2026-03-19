package com.finance.wallet.v12.domain.enums;

import java.util.Arrays;

public enum WalletStatus {
    ACTIVE("active"),
    FROZEN("frozen"),
    CLOSED("closed"),
    ;

    private final String value;

    WalletStatus(String value) {
        this.value = value;
    }

    public static WalletStatus fromValue(String value){
        return Arrays.stream(values()).filter(it -> it.value.equalsIgnoreCase(value))
                .findFirst().orElse(null);
    }
}
