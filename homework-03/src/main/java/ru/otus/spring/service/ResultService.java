package ru.otus.spring.service;

import ru.otus.spring.domain.TestResult;

import java.util.Locale;

public interface ResultService {
    void showResult(TestResult testResult, Locale locale);
}
