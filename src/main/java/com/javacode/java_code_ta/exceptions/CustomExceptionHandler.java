package com.javacode.java_code_ta.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        String message = String.join(", ", errors);

        log.debug("400 Bad Request {}", message, ex);
        return ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message(message)
                .reason("Incorrect parameters")
                .build();
    }

    @ExceptionHandler({IllegalArgumentException.class, BadRequestException.class, MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleBadRequestException(Exception e) {
        log.debug("400 Bad Request {}", e.getMessage(), e);
        return ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.toString())
                .message(e.getMessage())
                .reason("Incorrect parameters")
                .build();
    }

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleEntityNotFoundException(RuntimeException e) {
        log.debug("404 Not Found {}", e.getMessage(), e);
        return ExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND.toString())
                .message(e.getMessage())
                .reason("Object has not been found")
                .build();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({DataConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionResponse handleDataConflictException(RuntimeException e) {
        log.debug("409 Conflict {}", e.getMessage(), e);
        return ExceptionResponse.builder()
                .status(HttpStatus.CONFLICT.toString())
                .message(e.getMessage())
                .reason("Conflicting data")
                .build();
    }

}