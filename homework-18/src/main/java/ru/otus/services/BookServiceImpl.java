package ru.otus.services;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.UpdateBookDto;
import ru.otus.dto.AuthorDto;
import ru.otus.dto.GenreDto;
import ru.otus.exceptions.DataBaseErrorException;
import ru.otus.exceptions.NotFoundException;
import ru.otus.mappers.BookMapper;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.models.Response;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.GenreRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "library", fallbackMethod = "fallbackBook")
    public BookDto findById(long id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto)
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    @CircuitBreaker(name = "library", fallbackMethod = "fallbackBooks")
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "library", fallbackMethod = "fallbackBook")
    public BookDto create(CreateBookDto bookDto) {
        var author = getAuthorById(bookDto.getAuthorId());
        var genres = getGenresByIds(bookDto.getGenreIds());
        return bookMapper.toDto(bookRepository.save(bookMapper.toEntity(bookDto, author, genres)));
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "library", fallbackMethod = "fallbackBookException")
    public Book update(UpdateBookDto bookDto) {
        bookRepository.findById(bookDto.getId())
                .orElseThrow(() -> new NotFoundException("Book with id %d not found".formatted(bookDto.getId())));
        var author = getAuthorById(bookDto.getAuthorId());
        var genres = getGenresByIds(bookDto.getGenreIds());
        return bookRepository.save(bookMapper.toEntity(bookDto, author, genres));
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "library", fallbackMethod = "fallbackBookException")
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }

    private Author getAuthorById(long authorId) {
        return authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(authorId)));
    }

    private Set<Genre> getGenresByIds(Set<Long> genresIds) {
        if (genresIds == null || genresIds.isEmpty()) {
            throw new NotFoundException("Genre list is empty");
        }
        var genres = genreRepository.findAllById(genresIds);
        if (genresIds.size() != genres.size()) {
            throw new NotFoundException("Genres with ids %s not found".formatted(genresIds));
        }
        return new HashSet<>(genres);
    }

    private void fallbackBookException (long id, Throwable throwable) {
        log.error("Произошла ошибка при обращении в БД", throwable);
        throw new DataBaseErrorException(throwable.getMessage());
    }

    private Book fallbackBookException (UpdateBookDto bookDto, Throwable throwable) {
        log.error("Произошла ошибка при обращении в БД", throwable);
        throw new DataBaseErrorException(throwable.getMessage());
    }

    public BookDto fallbackBook(Throwable throwable) {
        log.error("Произошла ошибка при обращении в БД", throwable);
        return getEmptyBookDto();
    }

    private List<BookDto> fallbackBooks(Throwable throwable) {
        log.error("Произошла ошибка при обращении в БД", throwable);
        return List.of(getEmptyBookDto());
    }

    private BookDto getEmptyBookDto() {
        return new BookDto(-1L, "N/A",
                new AuthorDto(-1L, "N/A"),
                new GenreDto(-1L, "N/A"));
    }

    @ExceptionHandler(DataBaseErrorException.class)
    public Response handleException(DataBaseErrorException e) {
        return new Response(e.getMessage());
    }
}
