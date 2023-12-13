package ru.otus.services;

import ru.otus.dto.AuthorDto;
import ru.otus.models.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    List<AuthorDto> findAll();

    Optional<Author> findById(long id);
}
