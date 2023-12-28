package ru.otus.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.UpdateBookDto;
import ru.otus.models.Book;
import ru.otus.services.BookService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookRestController {
    private final BookService bookService;

    @GetMapping("/api/v1/books")
    public List<BookDto> getBooks() {
        return bookService.findAll();
    }

    @DeleteMapping("/api/v1/books/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }

    @PutMapping("/api/v1/books")
    public void updateBook(@Valid @RequestBody UpdateBookDto book) {
        bookService.update(book);
    }

    @PostMapping("/api/v1/books")
    public ResponseEntity<Book> create(@Valid @RequestBody CreateBookDto book) {
        return new ResponseEntity<>(bookService.create(book), HttpStatus.CREATED);
    }
}
