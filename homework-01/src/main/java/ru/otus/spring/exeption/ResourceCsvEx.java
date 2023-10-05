package ru.otus.spring.exeption;

import java.io.IOException;

public class ResourceCsvEx extends RuntimeException {
    public ResourceCsvEx(String msg, Throwable ex) {
        super(msg, ex);
    }
}