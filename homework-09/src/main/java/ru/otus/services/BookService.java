package ru.otus.services;

import ru.otus.dto.BookDto;
import ru.otus.dto.CreateBookDto;
import ru.otus.dto.UpdateBookDto;
import ru.otus.models.Book;

import java.util.List;

public interface BookService {
    BookDto findById(long id);

    List<BookDto> findAll();

    Book create(CreateBookDto bookDto, String genres);

    Book update(UpdateBookDto bookDto, String genres);

    void deleteById(long id);
}
