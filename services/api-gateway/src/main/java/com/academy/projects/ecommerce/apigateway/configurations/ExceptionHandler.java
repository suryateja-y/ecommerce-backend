package com.academy.projects.ecommerce.apigateway.configurations;

import com.academy.projects.ecommerce.apigateway.exceptions.TokenValidationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handle(Exception ex) {
        Throwable cause = ex.getCause();
        if(cause instanceof TokenValidationException)
            return new ResponseEntity<>(cause.getMessage(), HttpStatus.UNAUTHORIZED);
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
