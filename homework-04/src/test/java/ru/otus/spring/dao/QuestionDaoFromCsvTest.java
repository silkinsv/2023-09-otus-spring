package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.config.TestFileNameProvider;
import ru.otus.spring.domain.Question;
import ru.otus.spring.util.DataProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Question reader from file class test")
class QuestionDaoFromCsvTest {
    private final TestFileNameProvider fileNameProvider = Mockito.mock(TestFileNameProvider.class);

    private final QuestionDaoFromCsv questionDao = new QuestionDaoFromCsv(fileNameProvider);

    private final DataProvider dataProvider = new DataProvider();

    @DisplayName("get questions is correct in english")
    @Test
    void getAllEn() {
        List<Question> expectedQuestions = dataProvider.getQuestionsEn();
        when(fileNameProvider.getTestFileName()).thenReturn("testing_en.csv");
        List<Question> questions = questionDao.getAll();
        assertEquals(expectedQuestions, questions);
    }

    @DisplayName("get questions is correct in russian")
    @Test
    void getAllRus() {
        List<Question> expectedQuestions = dataProvider.getQuestionsRus();
        when(fileNameProvider.getTestFileName()).thenReturn("testing_ru_RU.csv");
        List<Question> questions = questionDao.getAll();
        assertEquals(expectedQuestions, questions);
    }
}
