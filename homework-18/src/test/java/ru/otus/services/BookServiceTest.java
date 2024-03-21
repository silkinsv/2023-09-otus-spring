package ru.otus.services;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import ru.otus.dto.AuthorDto;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.GenreDto;
import ru.otus.exceptions.NotFoundException;
import ru.otus.mappers.BookMapper;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.repositories.BookRepository;
import ru.otus.utils.DataProvider;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Тестирование Book Service")
@SpringBootTest
class BookServiceTest {
    @MockBean
    BookRepository bookRepository;

    @SpyBean
    BookService bookServiceMock;

    @MockBean
    BookMapper bookMapper;

    @Autowired
    BookService bookService;

    final DataProvider dataProvider = new DataProvider();

    final long FIRST_BOOK_ID = 1;

//    @Autowired
//    protected CircuitBreakerRegistry registry;

    @DisplayName("Должен возвращать книгу по Id")
    @Test
    void findBookByIdTest() {
        BookDto expectedBookDto = dataProvider.getBookDtoList().get(0);
        Book expectedBook = dataProvider.getBookList().get(0);
        when(bookRepository.findById(1)).thenReturn(Optional.of(expectedBook));
        when(bookMapper.toDto(expectedBook)).thenReturn(expectedBookDto);
        BookDto actualBookDto = bookService.findById(FIRST_BOOK_ID);
        assertEquals(expectedBookDto, actualBookDto);
    }

    @DisplayName("Должен возвращать все книги")
    @Test
    void findAllBookByIdTest() {
        List<BookDto> expectedBookDtoList = dataProvider.getBookDtoList();
        List<Book> expectedBookList = dataProvider.getBookList();

        when(bookRepository.findAll()).thenReturn(expectedBookList);
        when(bookMapper.toDto(expectedBookList.get(0))).thenReturn(expectedBookDtoList.get(0));
        when(bookMapper.toDto(expectedBookList.get(1))).thenReturn(expectedBookDtoList.get(1));
        when(bookMapper.toDto(expectedBookList.get(2))).thenReturn(expectedBookDtoList.get(2));
        List<BookDto> actualBookDtoList = bookService.findAll();
        assertEquals(expectedBookDtoList, actualBookDtoList);
    }

    @Test
    void shouldReturnEmptyBookList() {
        BookDto expectedResult = new BookDto(-1L, "N/A",
                new AuthorDto(-1L, "N/A"),
                new GenreDto(-1L, "N/A"));

        when(bookRepository.findById(100)).thenReturn(Optional.empty());
        BookDto actualResult = bookService.findById(100);
        verify(bookServiceMock).fallbackBook(any(NotFoundException.class));
        assertEquals(actualResult, expectedResult);
    }
}
