package ru.otus.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.UpdateBookDto;
import ru.otus.exceptions.NotFoundException;
import ru.otus.mappers.BookMapper;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    @GetMapping("/api/v1/books")
    public Flux<BookDto> getBooks() {
        return bookRepository.findAll().publishOn(Schedulers.boundedElastic())
                .map(bookMapper::toDto);
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<BookDto> getBookById(@PathVariable("id") String id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto);
    }

    @DeleteMapping("/api/v1/books/{id}")
    public void deleteBook(@PathVariable("id") String id) {
        bookRepository.deleteById(id).subscribe();
    }

    @PutMapping("/api/v1/books/{id}")
    public Mono<ResponseEntity<BookDto>> updateBook(@PathVariable("id") String id
            , @Valid @RequestBody UpdateBookDto book) {
        book.setId(id);
        return getBookFromUpdateBookDto(book)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(bookRepository::save)
                .map(bookMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/v1/books")
    public Mono<ResponseEntity<BookDto>>create(@Valid @RequestBody CreateBookDto book) {
        return getBookFromCreateBookDto(book)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(bookRepository::save)
                .map(bookMapper::toDto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    private Mono<Book> getBookFromCreateBookDto (CreateBookDto createBookDto) {
        return Mono.zip(
                        Mono.just(createBookDto.getTitle()),
                        authorRepository.findById(createBookDto.getAuthorId())
                                .switchIfEmpty(Mono.error(new NotFoundException("Author with id %s not found"
                                        .formatted(createBookDto.getAuthorId())))),
                        genreRepository.findById(createBookDto.getGenreId())
                                .switchIfEmpty(Mono.error(new NotFoundException("Genre with id %s not found"
                                        .formatted(createBookDto.getGenreId()))))
                )
                .flatMap(data -> {
                    Author author = data.getT2();
                    Genre genre = data.getT3();
                    Book book = new Book(data.getT1(), author, genre);
                    return Mono.just(book);
                })
                .onErrorResume(e -> Mono.empty());
    }

    private Mono<Book> getBookFromUpdateBookDto (UpdateBookDto updateBookDto) {
        return Mono.zip(
                        Mono.just(updateBookDto.getTitle()),
                        authorRepository.findById(updateBookDto.getAuthorId())
                                .switchIfEmpty(Mono.error(new NotFoundException("Author with id %s not found"
                                        .formatted(updateBookDto.getAuthorId())))),
                        genreRepository.findById(updateBookDto.getGenreId())
                                .switchIfEmpty(Mono.error(new NotFoundException("Genre with id %s not found"
                                        .formatted(updateBookDto.getGenreId()))))
                )
                .flatMap(data -> {
                    Author author = data.getT2();
                    Genre genre = data.getT3();
                    Book book = new Book(updateBookDto.getId(), data.getT1(), author, genre);
                    return Mono.just(book);
                })
                .onErrorResume(e -> Mono.empty());
    }
}
