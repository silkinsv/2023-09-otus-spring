package ru.otus.utils;

import ru.otus.dto.*;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DataProvider {
    private final List<BookDto> bookDtoList;

    private final List<AuthorDto> authorDtoList;

    private final List<GenreDto> genreDtoList;

    private final List<Book> bookList;

    private final CreateBookDto createBookDto;

    private final UpdateBookDto updateBookDto;

    public DataProvider() {
        authorDtoList = new ArrayList<>();
        authorDtoList.add(new AuthorDto("1L", "Author_1"));
        authorDtoList.add(new AuthorDto("2L", "Author_2"));
        authorDtoList.add(new AuthorDto("3L", "Author_3"));

        genreDtoList = new ArrayList<>();
        genreDtoList.add(new GenreDto("1L", "Genre_1"));
        genreDtoList.add(new GenreDto("2L", "Genre_2"));
        genreDtoList.add(new GenreDto("3L", "Genre_3"));
        genreDtoList.add(new GenreDto("4L", "Genre_4"));
        genreDtoList.add(new GenreDto("5L", "Genre_5"));
        genreDtoList.add(new GenreDto("6L", "Genre_6"));

        bookDtoList = new ArrayList<>();
        bookDtoList.add(new BookDto("1L", "BookTitle_1",
                authorDtoList.get(0),
                genreDtoList.get(0)));
        bookDtoList.add(new BookDto("2L", "BookTitle_2",
                authorDtoList.get(1),
                genreDtoList.get(2)));
        bookDtoList.add(new BookDto("3L", "BookTitle_3",
                authorDtoList.get(2),
                genreDtoList.get(4)));

        bookList = new ArrayList<>();
        bookList.add(new Book("1L",
                "BookTitle_1",
                new Author("1L", "Author_1"),
                Set.of(new Genre("1L", "Genre_1"), new Genre("2L", "Genre_2"))));
        bookList.add(new Book("2L",
                "BookTitle_2",
                new Author("2L", "Author_2"),
                Set.of(new Genre("3L", "Genre_3"), new Genre("4L", "Genre_4"))));
        bookList.add(new Book("3L",
                "BookTitle_3",
                new Author("3L", "Author_3"),
                Set.of(new Genre("5L", "Genre_5"), new Genre("6L", "Genre_6"))));

        createBookDto = new CreateBookDto("BookTitle_4", "2L", Set.of("3L"));

        updateBookDto = new UpdateBookDto("1L", "BookTitle_11", "1L", Set.of("1L"));
    }

    public List<BookDto> getBookDtoList() {
        return bookDtoList;
    }

    public List<AuthorDto> getAuthorDtoList() {
        return authorDtoList;
    }

    public List<GenreDto> getGenreDtoList() {
        return genreDtoList;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public CreateBookDto getCreateBookDto() {
        return createBookDto;
    }

    public UpdateBookDto getUpdateBookDto() {
        return updateBookDto;
    }
}
