package ru.otus.spring.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.util.DataProvider;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TestingServiceImplTest {
    private final QuestionDao questionDao = Mockito.mock(QuestionDao.class);
    private final IOService ioService = Mockito.mock(ConsoleIOService.class);
    private final TestingService service = new TestingServiceImpl(questionDao, ioService);
    final DataProvider dataProvider = new DataProvider();

    @Test
    void executeTestForPassTest() {
        Student student = dataProvider.getStudent();
        List<Question> questions = dataProvider.getQuestions();
        when(questionDao.getAll()).thenReturn(questions);
        when(ioService.readListIntForRange(1,2, "Error read answer, please try again"))
                .thenReturn(List.of(1));
        assertEquals(dataProvider.getTestResultCorrect(), service.executeTestFor(student));
        verify(questionDao, times(1)).getAll();
        verify(ioService, times(2)).readListIntForRange(1,2, "Error read answer, please try again");
    }

    @Test
    void executeTestForFailTest() {
        Student student = dataProvider.getStudent();
        List<Question> questions = dataProvider.getQuestions();
        when(questionDao.getAll()).thenReturn(questions);
        when(ioService.readListIntForRange(1,2, "Error read answer, please try again")).thenReturn(List.of(2));
        assertEquals(dataProvider.getTestResultWrong(), service.executeTestFor(student));
        verify(questionDao, times(1)).getAll();
        verify(ioService, times(2)).readListIntForRange(1,2, "Error read answer, please try again");
    }
}
