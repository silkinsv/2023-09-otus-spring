package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.util.DataProvider;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Testing service test")
class TestingServiceImplTest {
    private final QuestionDao questionDao = Mockito.mock(QuestionDao.class);
    private final IOService ioService = Mockito.mock(ConsoleIOService.class);
    private final LocalizedMessagesService localizedMessagesService = Mockito.mock(LocalizedMessagesServiceImpl.class);
    private final TestingService service = new TestingServiceImpl(questionDao, ioService, localizedMessagesService);
    final DataProvider dataProvider = new DataProvider();

    @DisplayName("test pass in english")
    @Test
    void executeTestForPassTest() {
        Student student = dataProvider.getStudent();
        List<Question> questions = dataProvider.getQuestionsEn();
        Locale locale = new Locale.Builder().setLanguage("en").build();
        when(questionDao.getAll()).thenReturn(questions);
        when(ioService.readListIntForRange(1,2, "Error read answer, please try again"))
                .thenReturn(List.of(1));
        assertEquals(dataProvider.getTestResultCorrect(), service.executeTestFor(student));
        verify(questionDao, times(1)).getAll();
        verify(ioService, times(2)).readListIntForRange(1,2, "Error read answer, please try again");
    }

    @DisplayName("test fail in english")
    @Test
    void executeTestForFailTest() {
        Student student = dataProvider.getStudent();
        List<Question> questions = dataProvider.getQuestionsEn();
        Locale locale = new Locale("en");
        when(questionDao.getAll()).thenReturn(questions);
        when(ioService.readListIntForRange(1,2, "Error read answer, please try again")).thenReturn(List.of(2));
        assertEquals(dataProvider.getTestResultWrong(), service.executeTestFor(student));
        verify(questionDao, times(1)).getAll();
        verify(ioService, times(2)).readListIntForRange(1,2, "Error read answer, please try again");
    }
}
