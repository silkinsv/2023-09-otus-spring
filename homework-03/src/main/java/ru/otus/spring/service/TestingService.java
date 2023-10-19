package ru.otus.spring.service;

import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.TestResult;

import java.util.Locale;

public interface TestingService {
    TestResult executeTestFor(Student student, Locale locale);
}
