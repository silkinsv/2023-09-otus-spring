package ru.otus.spring.util;

import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.TestResult;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    private final List<Question> questions;
    private final Student student = new Student("First_Name_Test", "Last_Name_Test");
    private final TestResult testResultCorrect = new TestResult(student);
    private final TestResult testResultWrong = new TestResult(student);

    public DataProvider() {
        questions = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Correct Answer", true));
        answers.add(new Answer("Wrong Answer", false));
        questions.add(new Question("First question", answers));
        questions.add(new Question("Second question", answers));

        for (Question question : questions) {
            testResultCorrect.applyAnswer(question, true);
        }

        for (Question question : questions) {
            testResultWrong.applyAnswer(question, false);
        }
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public Student getStudent() {
        return student;
    }

    public TestResult getTestResultCorrect() {
        return testResultCorrect;
    }

    public TestResult getTestResultWrong() {
        return testResultWrong;
    }
}
