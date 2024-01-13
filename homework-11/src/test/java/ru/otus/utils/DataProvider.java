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

    private final CreateBookDto createBookDto;

    private final UpdateBookDto updateBookDto;

    private final List<Author> authorList;

    private final List<Genre> genreList;

    private final List<Book> bookList;

    private final Book createBook;

    private final Book updateBook;

    private final BookDto newBook;

    private final BookDto editBook;

    public DataProvider() {
        authorDtoList = new ArrayList<>();
        authorDtoList.add(new AuthorDto("1L", "Author_1"));
        authorDtoList.add(new AuthorDto("2L", "Author_2"));
        authorDtoList.add(new AuthorDto("3L", "Author_3"));

        authorList = new ArrayList<>();
        authorList.add(new Author("1L", "Author_1"));
        authorList.add(new Author("2L", "Author_2"));
        authorList.add(new Author("3L", "Author_3"));

        genreDtoList = new ArrayList<>();
        genreDtoList.add(new GenreDto("1L", "Genre_1"));
        genreDtoList.add(new GenreDto("2L", "Genre_2"));
        genreDtoList.add(new GenreDto("3L", "Genre_3"));
        genreDtoList.add(new GenreDto("4L", "Genre_4"));
        genreDtoList.add(new GenreDto("5L", "Genre_5"));
        genreDtoList.add(new GenreDto("6L", "Genre_6"));

        genreList = new ArrayList<>();
        genreList.add(new Genre("1L", "Genre_1"));
        genreList.add(new Genre("2L", "Genre_2"));
        genreList.add(new Genre("3L", "Genre_3"));
        genreList.add(new Genre("4L", "Genre_4"));
        genreList.add(new Genre("5L", "Genre_5"));
        genreList.add(new Genre("6L", "Genre_6"));

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

        newBook = new BookDto("4L", "BookTitle_4",
                authorDtoList.get(1),
                genreDtoList.get(2));
        editBook = new BookDto("1L", "BookTitle_11",
                authorDtoList.get(0),
                genreDtoList.get(0));

        bookList = new ArrayList<>();
        bookList.add(new Book("1L", "BookTitle_1",
                authorList.get(0),
                genreList.get(0)));
        bookList.add(new Book("2L", "BookTitle_2",
                authorList.get(1),
                genreList.get(2)));
        bookList.add(new Book("3L", "BookTitle_3",
                authorList.get(2),
                genreList.get(4)));

        createBookDto = new CreateBookDto("BookTitle_4", "2L", "3L");
        createBook = new Book("4L", "BookTitle_4", authorList.get(1), genreList.get(2));

        updateBookDto = new UpdateBookDto("1L", "BookTitle_11", "1L", "1L");
        updateBook = new Book("1L", "BookTitle_11", authorList.get(0), genreList.get(0));
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

    public CreateBookDto getCreateBookDto() {
        return createBookDto;
    }

    public UpdateBookDto getUpdateBookDto() {
        return updateBookDto;
    }

    public List<Author> getAuthorList() {
        return authorList;
    }

    public List<Genre> getGenreList() {
        return genreList;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public Book getCreateBook() {
        return createBook;
    }

    public Book getUpdateBook() {
        return updateBook;
    }

    public BookDto getNewBook() {
        return newBook;
    }

    public BookDto getEditBook() {
        return editBook;
    }
}
