package ru.otus.spring.service;

import org.springframework.stereotype.Service;

@Service
public class ConsoleIOService implements IOService {
    public void println(String line) {
        System.out.println(line);
    }
}
