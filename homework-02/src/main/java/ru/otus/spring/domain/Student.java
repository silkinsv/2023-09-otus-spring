package ru.otus.spring.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Student {
    private final String firstName;
    private final String lastName;

    public String getFullName() {
        return String.format("%s %s", firstName, lastName);
    }
}
