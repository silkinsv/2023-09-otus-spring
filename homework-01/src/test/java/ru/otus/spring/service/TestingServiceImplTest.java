package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.util.DataProvider;

import java.util.List;

import static org.mockito.Mockito.*;

class TestingServiceImplTest {
    private final QuestionDao questionDao = Mockito.mock(QuestionDao.class);
    private final IOService ioService = Mockito.mock(ConsoleIOService.class);
    private final TestingService service = new TestingServiceImpl(questionDao, ioService);
    final DataProvider dataProvider = new DataProvider();

    @Test
    void printAllQuestions() {
        List<Question> questions = dataProvider.getQuestions();
        when(questionDao.getAll()).thenReturn(questions);
        service.printAllQuestions();
        verify(questionDao, times(1)).getAll();
    }
}
