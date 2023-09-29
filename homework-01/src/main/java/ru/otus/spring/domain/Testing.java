package ru.otus.spring.domain;

import java.util.List;

public class Testing {
    private final List<Question> questions;

    public Testing(List<Question> questions) {
        this.questions = questions;
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
