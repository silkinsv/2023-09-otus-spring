package ru.otus.services;

import ru.otus.dto.BookDto;
import ru.otus.dto.SaveBookDto;
import ru.otus.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    Book save(SaveBookDto bookDto);

    void deleteById(long id);
}
