package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.web.rest.errors.AbstractApiException;
import com.mycompany.myapp.web.rest.errors.Api000Exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdviceException extends ResponseEntityExceptionHandler {

    public ControllerAdviceException() {
        super();
    }

    @ExceptionHandler(value = { Api000Exception.class })
    public ResponseEntity<?> handle(final AbstractApiException apiException) {
        return new ResponseEntity<>(apiException.getBody(), HttpStatus.FORBIDDEN);
    }
}
