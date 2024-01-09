package ru.otus.services;

import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.UpdateBookDto;

import java.util.List;

public interface BookService {
    BookDto findById(String id);

    List<BookDto> findAll();

    BookDto create(CreateBookDto bookDto);

    BookDto update(UpdateBookDto bookDto);

    void deleteById(String id);
}
