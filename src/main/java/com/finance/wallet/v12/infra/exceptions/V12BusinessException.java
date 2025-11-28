package com.finance.wallet.v12.infra.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class V12BusinessException extends RuntimeException{
    private String title;
    private String type;
    private HttpStatus http;
    public V12BusinessException(String message, String title, HttpStatus http)
    {
        super(message);
        this.title = title;
        this.http = http;
        this.type = "v12bank/error/api/services";
    }

    public V12BusinessException(String message, HttpStatus http)
    {
        super(message);
        this.http = http;
        this.type = "v12bank/error/api/services";
    }

    public static V12BusinessException businessRule(String message)
    {
        return new V12BusinessException(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public static V12BusinessException notFound(String message)
    {
        return new V12BusinessException(message, HttpStatus.NOT_FOUND);
    }

}
