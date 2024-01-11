package ru.otus.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.UpdateBookDto;
import ru.otus.services.BookService;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public Flux<BookDto> getBooks() {
        return bookService.findAll();
    }

    @GetMapping("/api/v1/books/{id}")
    public Mono<BookDto> getBookById(@PathVariable("id") String id) {
        return bookService.findById(id);
    }

    @DeleteMapping("/api/v1/books/{id}")
    public void deleteBook(@PathVariable("id") String id) {
        bookService.deleteById(id);
    }

    @PutMapping("/api/v1/books/{id}")
    public Mono<BookDto> updateBook(@PathVariable("id") String id, @Valid @RequestBody UpdateBookDto book) {
        book.setId(id);
        return bookService.update(book);
    }

    @PostMapping("/api/v1/books")
    public Mono<BookDto>create(@Valid @RequestBody CreateBookDto book) {
        return bookService.create(book);
    }
}
