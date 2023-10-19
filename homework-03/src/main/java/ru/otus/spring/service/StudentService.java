package ru.otus.spring.service;

import ru.otus.spring.domain.Student;

import java.util.Locale;

public interface StudentService {
    Student registerStudent(Locale locale);
}
