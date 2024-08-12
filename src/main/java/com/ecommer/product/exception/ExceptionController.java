package com.ecommer.product.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public String illegalArgumentExceptionHandler(IllegalArgumentException e) {
        return e.getMessage();
    }
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NullPointerException.class)
    public String nullPointerExceptionHandler(NullPointerException e) {
        return e.getMessage();
    }
    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(Exception e) {
        return e.getMessage();
    }
}
