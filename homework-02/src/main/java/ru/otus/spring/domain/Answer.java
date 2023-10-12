package ru.otus.spring.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Answer {
    private final String text;

    private final boolean isCorrect;

    public String getText() {
        return text;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
