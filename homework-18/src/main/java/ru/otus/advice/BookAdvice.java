package ru.otus.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.otus.exceptions.DataBaseErrorException;
import ru.otus.models.Response;

@ControllerAdvice
public class BookAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler(DataBaseErrorException.class)
    public ResponseEntity<Response> handleException(DataBaseErrorException e) {
        Response response = new Response(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }
}
