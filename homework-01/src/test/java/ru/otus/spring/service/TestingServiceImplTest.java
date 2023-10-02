package ru.otus.spring.service;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.spring.dao.TestingDao;
import ru.otus.spring.domain.Testing;
import ru.otus.spring.util.DataProvider;

import static org.mockito.Mockito.*;

class TestingServiceImplTest {
    private final TestingDao testingDao = Mockito.mock(TestingDao.class);
    private final TestingService service = new TestingServiceImpl(testingDao);
    final DataProvider dataProvider = new DataProvider();

    @Test
    void printAllQuestions() {
        Testing testing = dataProvider.getTesting();
        when(testingDao.find()).thenReturn(testing);
        service.printAllQuestions();
        verify(testingDao, times(1)).find();
    }
}
