package ru.otus.spring.service;

import ru.otus.spring.dao.TestingDao;
import ru.otus.spring.domain.Testing;

public class TestingServiceImpl implements TestingService {
    private final TestingDao testingDao;

    public TestingServiceImpl(TestingDao testingDao) {
        this.testingDao = testingDao;
    }

    public void printAllQuestions() {
        Testing test = testingDao.find();
        test.getQuestions().forEach(q -> {
            System.out.println(q.getText());
            q.getAnswers().forEach(a -> {
                System.out.println("* " + a.getText());
            });
        });
    }
}
