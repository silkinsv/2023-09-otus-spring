package ru.otus.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.UpdateBookDto;
import ru.otus.mappers.BookMapper;
import ru.otus.models.Author;
import ru.otus.models.Book;
import ru.otus.models.Genre;
import ru.otus.repositories.AuthorRepository;
import ru.otus.repositories.BookRepository;
import ru.otus.repositories.GenreRepository;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    @Override
    @Transactional(readOnly = true)
    public Mono<BookDto> findById(String id) {
        return bookRepository.findById(id)
                .map(bookMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Flux<BookDto> findAll() {
        return bookRepository.findAll().publishOn(Schedulers.boundedElastic())
                .map(bookMapper::toDto);
    }

    @Override
    @Transactional
    public Mono<BookDto> create(CreateBookDto bookDto) {
        return getBook(null, bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId())
                .publishOn(Schedulers.boundedElastic())
                .flatMap(bookRepository::save)
                .map(bookMapper::toDto);
    }

    @Override
    @Transactional
    public Mono<BookDto> update(UpdateBookDto bookDto) {
        return getBook(bookDto.getId(), bookDto.getTitle(), bookDto.getAuthorId(), bookDto.getGenreId())
                .publishOn(Schedulers.boundedElastic())
                .flatMap(bookRepository::save)
                .map(bookMapper::toDto);
    }

    @Override
    @Transactional
    public Mono<Void> deleteById(String id) {
        return bookRepository.deleteById(id);
    }

    private Mono<Book> getBook (String id, String title, String authorId, String genreId) {
        return Mono.zip(
                        Mono.just(title),
                        authorRepository.findById(authorId),
                        genreRepository.findById(genreId)
                )
                .flatMap(data -> {
                    Author author = data.getT2();
                    Genre genre = data.getT3();
                    Book book;
                    if (id == null) {
                        book = new Book(data.getT1(), author, genre);
                    } else {
                        book = new Book(id, data.getT1(), author, genre);
                    }
                    return Mono.just(book);
                });
    }
}
