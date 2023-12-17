package ru.otus.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.UpdateBookDto;
import ru.otus.exceptions.NotFoundException;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.GenreRepository;

import java.util.Set;
import java.util.List;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.HashSet;


@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    public Book toEntity(CreateBookDto bookDto) {
        return new Book(null,
                bookDto.getTitle(),
                getAuthorById(bookDto.getAuthorId()),
                getGenresByNames(bookDto.getGenres()));
    }

    public Book toEntity(UpdateBookDto bookDto) {
        return new Book(bookDto.getId(),
                bookDto.getTitle(),
                getAuthorById(bookDto.getAuthorId()),
                getGenresByNames(bookDto.getGenres()));
    }

    public BookDto toDto(Book book) {
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

    private Author getAuthorById(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(authorId)));
    }

    private Set<Genre> getGenresByNames(String genresString) {
        if (genresString == null || genresString.isEmpty()) {
            throw new NotFoundException("Genre list is empty");
        }

        List<String> genreList = new ArrayList<>();
        for (String genre : genresString.split(",")) {
            genre = genre.replace('[', ' ').replace(']', ' ').trim();
            genreList.add(genre);
        }

        var genres = genreRepository.findAllByNameIn(genreList);
        if (genreList.size() != genres.size()) {
            throw new NotFoundException("Genres with names %s not found".formatted(genreList));
        }
        return new HashSet<>(genres);
    }
}
