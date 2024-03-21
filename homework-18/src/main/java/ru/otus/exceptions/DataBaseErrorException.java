package ru.otus.exceptions;

public class DataBaseErrorException extends RuntimeException {
    public DataBaseErrorException(String message) {
        super(message);
    }
}