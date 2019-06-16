package com.mk.orderbooks.exception;

import com.mk.orderbooks.service.Messages;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.mk.orderbooks.junk.MessageKeys.*;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Messages messages;

    public RestExceptionHandler(Messages messages) {
        this.messages = messages;
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(
            NoSuchElementException ex, WebRequest request) {
        return handleExceptionInternal(ex, messages.getMessage(ERROR_NOT_FOUND), new HttpHeaders(), NOT_FOUND, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return handleExceptionInternal(ex, messages.getMessage(ERROR_REQUEST_NOT_CONSISTENT), new HttpHeaders(), BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = ex.getBindingResult().getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining(" - "));
        return handleExceptionInternal(ex, message, new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), PRECONDITION_FAILED, request);
    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<Object> handleIllegalStateException(
            IllegalStateException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), PRECONDITION_FAILED, request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> unhandledException(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, messages.getMessage(ERROR_UNHANDLED), new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }

}