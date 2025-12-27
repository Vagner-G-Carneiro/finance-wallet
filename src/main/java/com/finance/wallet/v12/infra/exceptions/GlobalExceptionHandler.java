package com.finance.wallet.v12.infra.exceptions;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(V12BusinessException.class)
    public ProblemDetail handleAllV12Exceptions(V12BusinessException businessException)
    {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(businessException.getHttp(), businessException.getMessage());
        problem.setTitle(businessException.getTitle());
        problem.setType(URI.create(businessException.getType()));
        problem.setProperty("timestamp", Instant.now());
        log.error("EXCEPTION V12's => {}", businessException.getMessage());
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentValidException(MethodArgumentNotValidException method)
    {
        Map<String, String> listErros = new HashMap<String, String>();
        method.getAllErrors().forEach(each -> {
            String field = ((FieldError) each).getField();
            String message = each.getDefaultMessage();
            listErros.put(field, message);
        });

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        problem.setProperty("erros", listErros);
        problem.setTitle(method.getClass().getSimpleName());
        problem.setProperty("timestamp", Instant.now());
        problem.setType(URI.create("v12bank/error/api/services"));
        log.error("EXCEPTION METHOD-ARGUMENT-VALID => {}", method.getMessage());
        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGlobalExceptions(Exception exception)
    {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado ao lado do servidor. Contate o Suporte.");
        problem.setProperty("timestamp", Instant.now());
        problem.setType(URI.create("v12bank/error/global/exceptions"));
        log.error("EXCEPTION => {}", exception.getMessage(), exception);
        return problem;
    }

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleRunTimeExceptions(RuntimeException run)
    {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado ao lado do servidor. Contate o Suporte.");
        problem.setProperty("timestamp", Instant.now());
        problem.setType(URI.create("v12bank/error/global/runtime"));
        log.error("EXCEPTION RUN-TIME=> {}", run.getMessage(), run);
        return problem;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(RuntimeException run)
    {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, "Oops, senha / email inválidos.");
        problem.setProperty("timestamp", Instant.now());
        problem.setType(URI.create("v12bank/error/auth"));
        log.error("EXCEPTION BAD-CREDENTIALS=> {}", run.getMessage(), run);
        return problem;
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ProblemDetail handleJWTVerificationException(JWTVerificationException run)
    {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Oops, seu token expirou, inicie uma nova sessão!.");
        problem.setProperty("timestamp", Instant.now());
        problem.setType(URI.create("v12bank/error/auth"));
        log.error("EXCEPTION TOKEN-EXPIRED-EXCEPTION {}", run.getMessage(), run);
        return problem;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleHttpMessageNotReadableException(HttpMessageNotReadableException run)
    {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Verifique o formato de entrada para valor e tente novamente!");
        problem.setProperty("timestamp", Instant.now());
        problem.setType(URI.create("v12bank/error/auth"));
        log.error("EXCEPTION TOKEN-EXPIRED-EXCEPTION {}", run.getMessage(), run);
        return problem;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException run)
    {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Metodo incompatível: " + run.getMessage());
        problem.setProperty("timestamp", Instant.now());
        problem.setType(URI.create("v12bank/error/auth"));
        log.error("EXCEPTION TOKEN-EXPIRED-EXCEPTION {}", run.getMessage(), run);
        return problem;
    }
}
