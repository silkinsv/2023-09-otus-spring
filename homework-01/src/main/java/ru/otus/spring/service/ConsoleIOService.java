package ru.otus.spring.service;

public class ConsoleIOService implements IOService {
    public void println(String line) {
        System.out.println(line);
    }
}
