package ru.otus.services.utils;

import ru.otus.dto.AuthorDto;
import ru.otus.dto.BookDto;
import ru.otus.dto.GenreDto;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;

import java.util.List;
import java.util.TreeSet;

public class DtoConverter {
    public static BookDto convertToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor().getFullName());
        List<String> genres = book.getGenres().stream()
                .map(Genre::getName)
                .toList();
        bookDto.setGenres(new TreeSet<>(genres).toString());
        return bookDto;
    }

    public static AuthorDto convertToAuthorDto(Author author) {
        return new AuthorDto(author.getId(), author.getFullName());
    }

    public static GenreDto convertToGenreDto(Genre genre) {
        return new GenreDto(genre.getId(), genre.getName());
    }
}
