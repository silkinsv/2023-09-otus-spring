package ru.otus.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.GenreDto;
import ru.otus.dto.UpdateBookDto;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;

import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public Book toEntity(CreateBookDto bookDto, Author author, Set<Genre> genres) {
        return new Book(null,
                bookDto.getTitle(),
                author,
                genres);
    }

    public Book toEntity(UpdateBookDto bookDto, Author author, Set<Genre> genres) {
        return new Book(bookDto.getId(),
                bookDto.getTitle(),
                author,
                genres);
    }

    public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(authorMapper.toDto(book.getAuthor()));
        Set<GenreDto> genres = book.getGenres().stream()
                .map(genreMapper::toDto)
                .collect(Collectors.toSet());
        bookDto.setGenre(genres.stream().findAny().get());
        return bookDto;
    }
}
