package com.finance.wallet.v12.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum OperationType {
    DEPOSIT("deposit"),
    TRANSFER("transfer"),
    REFUND("refund"),
    ;

    private final String value;

    OperationType(String value) {
        this.value = value;
    }

    public static OperationType fromValue(String value){
        return Arrays.stream(values()).filter(it -> it.value.equalsIgnoreCase(value))
                .findFirst().orElse(null);
    }

}
