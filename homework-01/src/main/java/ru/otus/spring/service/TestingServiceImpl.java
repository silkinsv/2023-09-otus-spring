package ru.otus.spring.service;

import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;

import java.util.List;

public class TestingServiceImpl implements TestingService {
    private final QuestionDao questionDao;
    private final IOService ioService;

    public TestingServiceImpl(QuestionDao questionDao, IOService ioService) {
        this.questionDao = questionDao;
        this.ioService = ioService;
    }

    public void printAllQuestions() {
        List<Question> questions = questionDao.getAll();
        questions.forEach(q -> {
            ioService.println(q.getText());
            q.getAnswers().forEach(a -> {
                ioService.println("* " + a.getText());
            });
        });
    }
}
