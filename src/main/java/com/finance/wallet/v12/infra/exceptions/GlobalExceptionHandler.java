package com.finance.wallet.v12.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(V12BusinessException.class)
    public ProblemDetail handleAllV12Exceptions(V12BusinessException businessException)
    {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, businessException.getMessage());
        problem.setTitle(businessException.getTitle());
        problem.setType(URI.create(businessException.getType()));
        problem.setProperty("timestamp", LocalDateTime.now());
        return problem;
    }

}
