package ru.otus.spring.util;

import ru.otus.spring.domain.Answer;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.domain.TestResult;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    private final List<Question> questionsEn;
    private final List<Question> questionsRus;
    private final Student student = new Student("First_Name_Test", "Last_Name_Test");
    private final TestResult testResultCorrect = new TestResult(student);
    private final TestResult testResultWrong = new TestResult(student);

    public DataProvider() {
        questionsEn = new ArrayList<>();
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Correct Answer", true));
        answers.add(new Answer("Wrong Answer", false));
        questionsEn.add(new Question("First question", answers));
        questionsEn.add(new Question("Second question", answers));

        for (Question question : questionsEn) {
            testResultCorrect.applyAnswer(question, true);
        }

        for (Question question : questionsEn) {
            testResultWrong.applyAnswer(question, false);
        }

        questionsRus = new ArrayList<>();
        answers = new ArrayList<>();
        answers.add(new Answer("Правильный ответ", true));
        answers.add(new Answer("Неправильный ответ", false));
        questionsRus.add(new Question("Первый вопрос", answers));
        questionsRus.add(new Question("Второй вопрос", answers));

//        for (Question question : questionsRus) {
//            testResultCorrect.applyAnswer(question, true);
//        }
//
//        for (Question question : questionsRus) {
//            testResultWrong.applyAnswer(question, false);
//        }
    }

    public List<Question> getQuestionsEn() {
        return questionsEn;
    }
    public List<Question> getQuestionsRus() {
        return questionsRus;
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
