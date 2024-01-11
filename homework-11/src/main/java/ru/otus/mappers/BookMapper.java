package ru.otus.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.UpdateBookDto;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    public Book toEntity(CreateBookDto bookDto, Author author, Genre genre) {
        return new Book(null,
                bookDto.getTitle(),
                author,
                genre);
    }

    public Book toEntity(UpdateBookDto bookDto, Author author, Genre genre) {
        return new Book(bookDto.getId(),
                bookDto.getTitle(),
                author,
                genre);
    }

    public BookDto toDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(authorMapper.toDto(book.getAuthor()));
        bookDto.setGenre(genreMapper.toDto(book.getGenre()));
        return bookDto;
    }
}
