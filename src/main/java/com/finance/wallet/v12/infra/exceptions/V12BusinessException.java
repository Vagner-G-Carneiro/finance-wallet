package com.finance.wallet.v12.infra.exceptions;

import lombok.Getter;

@Getter
public class V12BusinessException extends RuntimeException{
    private String title;
    private String type;
    public V12BusinessException(String message, String title)
    {
        super(message);
        this.title = title;
        this.type = "error";
    }
}
