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
    private final TestFileNameProvider testFileNameProvider = Mockito.mock(TestFileNameProvider.class);
    private final QuestionDaoFromCsv questionDao = new QuestionDaoFromCsv(testFileNameProvider);

    private final DataProvider dataProvider = new DataProvider();

    @DisplayName("get questions is correct")
    @Test
    void getAll() {
        List<Question> expectedQuestions = dataProvider.getQuestions();
        when(testFileNameProvider.getTestFileName()).thenReturn("testing.csv");
        List<Question> questions = questionDao.getAll();
        assertEquals(expectedQuestions, questions);
    }
}
