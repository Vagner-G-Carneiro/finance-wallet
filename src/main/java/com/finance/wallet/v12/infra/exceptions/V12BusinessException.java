package com.finance.wallet.v12.infra.exceptions;

import com.finance.wallet.v12.domain.enums.ErrorInfo;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class V12BusinessException extends RuntimeException{

    private final String type;
    private final HttpStatus http;
    private ErrorInfo errorInfo;

    public V12BusinessException(String message, HttpStatus http)
    {
        super(message);
        this.http = http;
        this.type = "v12bank/error/api/services";
    }

    public V12BusinessException(String message, HttpStatus http, ErrorInfo errorInfo)
    {
        super(message);
        this.http = http;
        this.type = "v12bank/error/api/services";
        this.errorInfo = errorInfo;
    }

    public static V12BusinessException businessRule(String message, ErrorInfo errorInfo)
    {
        return new V12BusinessException(message, HttpStatus.UNPROCESSABLE_ENTITY, errorInfo);
    }

    public static V12BusinessException notFound(String message)
    {
        return new V12BusinessException(message, HttpStatus.NOT_FOUND, null);
    }

}
