package ru.practicum.shareit.exceptions;


import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class AllExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, HttpStatus> handleConstraintViolationException(ConstraintViolationException ex) {
        return Map.of("Validation exception", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, HttpStatus> handleConstraintViolationException(BookingException ex) {
        return Map.of("Booking exception", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, HttpStatus> handleIllegalArgumentException(IllegalArgumentException ex) {
        return Map.of("Conflict", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, HttpStatus> handleRuntimeException(RuntimeException ex) {
        return Map.of("NotFound", HttpStatus.NOT_FOUND);
    }
}
