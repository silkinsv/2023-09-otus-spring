package ru.otus.spring.util;

import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    private final List<Question> questions;

    public DataProvider() {
        questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Correct Answer", true));
        answers.add(new Answer("Wrong Answer", false));
        questions.add(new Question("First question", answers));
        questions.add(new Question("Second question", answers));
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
