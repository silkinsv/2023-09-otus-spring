package ru.otus.spring.exeption;

public class ResourceCsvEx extends RuntimeException {
    public ResourceCsvEx(String msg, Throwable ex) {
        super(msg, ex);
    }
}