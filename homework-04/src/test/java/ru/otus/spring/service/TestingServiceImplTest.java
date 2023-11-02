package ru.otus.spring.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.spring.dao.QuestionDao;
import ru.otus.spring.domain.Question;
import ru.otus.spring.domain.Student;
import ru.otus.spring.util.DataProvider;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@DisplayName("Testing service test")
@SpringBootTest
class TestingServiceImplTest {
    @MockBean
    QuestionDao questionDao;
    @MockBean
    IOService ioService;
    @MockBean
    LocalizedMessagesService localizedMessagesService;
    @Autowired
    TestingService service;
    final DataProvider dataProvider = new DataProvider();

    @DisplayName("test pass in english")
    @Test
    void executeTestForPassTest() {
        Student student = dataProvider.getStudent();
        List<Question> questions = dataProvider.getQuestionsEn();
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
        when(questionDao.getAll()).thenReturn(questions);
        when(ioService.readListIntForRange(1,2, "Error read answer, please try again")).thenReturn(List.of(2));
        assertEquals(dataProvider.getTestResultWrong(), service.executeTestFor(student));
        verify(questionDao, times(1)).getAll();
        verify(ioService, times(2)).readListIntForRange(1,2, "Error read answer, please try again");
    }
}
