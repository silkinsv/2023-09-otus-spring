package ru.otus.spring.dao;

import org.junit.jupiter.api.Test;
import ru.otus.spring.config.AppConfig;
import ru.otus.spring.config.TestFileNameProvider;
import ru.otus.spring.domain.Question;
import ru.otus.spring.util.DataProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionDaoFromCsvTest {
    private final DataProvider dataProvider = new DataProvider();
    @Test
    void getAll() {
        List<Question> expectedQuestions = dataProvider.getQuestions();
        TestFileNameProvider appConfig = new AppConfig(3, "testing.csv");
        QuestionDao questionDao = new QuestionDaoFromCsv(appConfig);
        List<Question> questions = questionDao.getAll();
        assertEquals(expectedQuestions, questions);
    }
}
