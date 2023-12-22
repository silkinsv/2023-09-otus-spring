package ru.otus.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.mappers.BookMapper;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.repositories.BookRepository;
import ru.otus.utils.DataProvider;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("Тестирование Book Service")
@SpringBootTest
class BookServiceTest {
    @MockBean
    BookRepository bookRepository;

    @MockBean
    BookMapper bookMapper;

    @Autowired
    BookService bookService;

    final DataProvider dataProvider = new DataProvider();

    final long FIRST_BOOK_ID = 1;

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

    @DisplayName("Должен сохранять книгу")
    @Test
    void saveTest() {
        CreateBookDto bookDto = dataProvider.getCreateBookDto();
        Author author = new Author(2L, "Author_2");
        Set<Genre> genres = Set.of(new Genre(3L, "Genre_3"));
        Book book = new Book(null
                , "BookTitle_4"
                , author
                , new HashSet<>(genres));
        Book expectedBook = new Book(4L
                , "BookTitle_4"
                , author
                , new HashSet<>(genres));
        when(bookMapper.toEntity(bookDto, author, genres)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(expectedBook);
        Book actualBook = bookService.create(bookDto);
        assertEquals(expectedBook, actualBook);
    }
}
