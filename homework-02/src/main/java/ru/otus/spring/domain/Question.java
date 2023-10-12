package ru.otus.spring.domain;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class Question {
    private final String text;

    private final List<Answer> answers;

    public String getText() {
        return text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }
}
