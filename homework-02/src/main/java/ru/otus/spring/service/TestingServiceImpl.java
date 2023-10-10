package ru.otus.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;

import java.util.List;

@Service
public class TestingServiceImpl implements TestingService {
    private final QuestionDao questionDao;

    private final IOService ioService;

    @Autowired
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
