package ru.otus.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.exceptions.NotFoundException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessError() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Недоступно для вашей роли");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundError(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exceptionError(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}
