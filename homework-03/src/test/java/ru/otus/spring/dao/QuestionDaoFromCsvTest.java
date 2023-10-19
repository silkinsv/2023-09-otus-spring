package ru.otus.spring.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import ru.otus.spring.config.AppConfig;
import ru.otus.spring.domain.Question;
import ru.otus.spring.util.DataProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Question reader from file class test")
class QuestionDaoFromCsvTest {
    private final AppConfig appConfig = Mockito.mock(AppConfig.class);
    private final MessageSource messageSource = Mockito.mock(MessageSource.class);
    private final QuestionDaoFromCsv questionDao = new QuestionDaoFromCsv(appConfig, messageSource);

    private final DataProvider dataProvider = new DataProvider();

    @DisplayName("get questions is correct in english")
    @Test
    void getAllEn() {
        List<Question> expectedQuestions = dataProvider.getQuestionsEn();
        when(messageSource.getMessage("test.file.name",  null, appConfig.getLocale())).thenReturn("testing_en.csv");
        List<Question> questions = questionDao.getAll();
        assertEquals(expectedQuestions, questions);
    }

    @DisplayName("get questions is correct in russian")
    @Test
    void getAllRus() {
        List<Question> expectedQuestions = dataProvider.getQuestionsRus();
        when(messageSource.getMessage("test.file.name",  null, appConfig.getLocale())).thenReturn("testing_ru_RU.csv");
        List<Question> questions = questionDao.getAll();
        assertEquals(expectedQuestions, questions);
    }
}
