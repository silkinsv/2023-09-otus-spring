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

    final long FIRST_BOOK_ID = 1;

    @DisplayName("Должен возвращать книгу по Id")
    @Test
    void findBookByIdTest() {
        BookDto expectedBookDto = new BookDto(FIRST_BOOK_ID, "BookTitle_1", "Author_1", "[Genre_1, Genre_2]");
        Book expectedBook = new Book(FIRST_BOOK_ID
                , "BookTitle_1"
                , new Author(1L, "Author_1")
                , Set.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")));
        when(bookRepository.findById(1)).thenReturn(Optional.of(expectedBook));
        when(bookMapper.toDto(expectedBook)).thenReturn(expectedBookDto);
        BookDto actualBookDto = bookService.findById(FIRST_BOOK_ID);
        assertEquals(expectedBookDto, actualBookDto);
    }

    @DisplayName("Должен возвращать все книги")
    @Test
    void findAllBookByIdTest() {
        List<BookDto> expectedBookDtoList = List.of(new BookDto(1L, "BookTitle_1", "Author_1", "[Genre_1, Genre_2]")
                , new BookDto(2L, "BookTitle_2", "Author_2", "[Genre_3, Genre_4]")
                , new BookDto(3L, "BookTitle_3", "Author_3", "[Genre_5, Genre_6]"));

        List<Book> expectedBookList = List.of(new Book(1L, "BookTitle_1"
                        , new Author(1L, "Author_1")
                        , Set.of(new Genre(1L, "Genre_1"), new Genre(2L, "Genre_2")))
                , new Book(2L, "BookTitle_2"
                        , new Author(2L, "Author_2")
                        , Set.of(new Genre(3L, "Genre_3"), new Genre(4L, "Genre_4")))
                , new Book(3L, "BookTitle_3"
                        , new Author(3L, "Author_3")
                        , Set.of(new Genre(5L, "Genre_5"), new Genre(6L, "Genre_6"))));

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
        CreateBookDto bookDto = new CreateBookDto("BookTitle_4", 2L, "Genre_3, Genre_4");
        Author author = new Author(2L, "Author_2");
        List<Genre> genres = List.of(new Genre(3L, "Genre_3"), new Genre(4L, "Genre_4"));
        Book book = new Book(null
                , "BookTitle_4"
                , author
                , new HashSet<>(genres));
        Book expectedBook = new Book(4L
                , "BookTitle_4"
                , author
                , new HashSet<>(genres));
        when(bookMapper.toEntity(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(expectedBook);
        Book actualBook = bookService.save(bookDto);
        assertEquals(expectedBook, actualBook);
    }
}
