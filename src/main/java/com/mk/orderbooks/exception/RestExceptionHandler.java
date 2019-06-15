package com.mk.orderbooks.exception;

import com.mk.orderbooks.service.Messages;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

import static com.mk.orderbooks.junk.MessageKeys.ERROR_NOT_FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Messages messages;

    public RestExceptionHandler(Messages messages) {
        this.messages = messages;
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> resourceNotFoundException(
            NoSuchElementException ex, WebRequest request) {
        return handleExceptionInternal(ex, messages.getMessage(ERROR_NOT_FOUND), new HttpHeaders(), NOT_FOUND, request);
    }
}